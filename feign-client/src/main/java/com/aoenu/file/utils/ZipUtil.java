package com.aoenu.file.utils;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 通过Java的Zip输入输出流实现压缩和解压文件<br>
 * 
 * 注意：ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream("D:/abc.zip")));这句立即就会产生一个zip文件
 * 
 * @author liujiduo
 * 
 */
public final class ZipUtil {
	/**
	 * 打包成zip
	 * 
	 * @param fileList
	 *            文件或目录列表，如果是目录会递归打包
	 * @param zipPath
	 *            zip包生成路径。完整路径名，包括文件名和扩展名
	 * @return 返回zip文件，可以getAbsolutePath()获取到详细路径，或进一步获取到文件流
	 * @author Stone
	 */
	public static File genZipFile(List<File> fileList, String zipPath) {
		// 检查文件必须都存在
		checkFileExist(fileList);

		// 目标zip存在则删
		File zipFile = new File(zipPath);
		deleteFileIfExists(zipFile);

		// 打包
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(new BufferedOutputStream(fos));
			// 添加对应的文件Entry
			for (File file : fileList) {
				addEntry("", file, zos);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			closeResource(zos, fos);
		}
		return new File(zipPath);
	}

	/**
	 * 解压文件
	 * 
	 * @param filePath
	 *            压缩文件路径
	 */
	public static void unzip(String filePath) {
		File source = new File(filePath);
		if (source.exists()) {
			ZipInputStream zis = null;
			BufferedOutputStream bos = null;
			try {
				zis = new ZipInputStream(new FileInputStream(source));
				ZipEntry entry = null;
				while ((entry = zis.getNextEntry()) != null && !entry.isDirectory()) {
					File target = new File(source.getParent(), entry.getName());
					if (!target.getParentFile().exists()) {
						// 创建文件父目录
						target.getParentFile().mkdirs();
					}
					// 写入文件
					bos = new BufferedOutputStream(new FileOutputStream(target));
					int read = 0;
					byte[] buffer = new byte[1024 * 10];
					while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
						bos.write(buffer, 0, read);
					}
					bos.flush();
				}
				zis.closeEntry();
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				closeResource(zis, bos);
			}
		}
	}

	/**
	 * 扫描添加文件Entry
	 * 
	 * @param base
	 *            基路径
	 * 
	 * @param source
	 *            源文件
	 * @param zos
	 *            Zip文件输出流
	 * @throws IOException
	 */
	public static void addEntry(String base, File file, ZipOutputStream zos) throws IOException {
		checkFileExist(file);
		
		// 按目录分级，形如：/aaa/bbb.txt
		String entry = base + file.getName();
		if (file.isDirectory()) {
			for (File subFile : file.listFiles()) {
				// 递归列出目录下的所有文件，添加文件Entry
				addEntry(entry + "/", subFile, zos);
			}
		} else {
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				byte[] buffer = new byte[1024 * 10];
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis, buffer.length);
				int read = 0;
				zos.putNextEntry(new ZipEntry(entry));
				while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
					zos.write(buffer, 0, read);
				}
				zos.closeEntry();
			} finally {
				closeResource(bis, fis);
			}
		}
	}

	/**
	 * 往ZipOutputStream里添加文件
	 * 
	 * @param fileList
	 *            文件或目录列表，如果是目录会递归打包
	 * @param zos
	 *            ZipOutputStream文件流
	 * @author Stone
	 */
	public static void addEntry(List<File> fileList, ZipOutputStream zos) {
		try {
			for (File file : fileList) {
				addEntry("", file, zos);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			closeResource(zos);
		}
	}

	private static void closeResource(Closeable... closeables) {
		try {
			if (closeables != null) {
				for (Closeable closeable : closeables) {
					if (closeable != null) {
						closeable.close();
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void deleteFileIfExists(String zipPath) {
		File file = new File(zipPath);
		deleteFileIfExists(file);
	}

	private static void deleteFileIfExists(File zipPath) {
		if (zipPath.exists()) {
			zipPath.delete();
		}
	}

	private static void checkFileExist(List<File> fileList) {
		for (File file : fileList) {
			checkFileExist(file);
		}
	}
	
	private static void checkFileExist(File file) {
		if (!file.exists()) {
			throw new RuntimeException("file not exist:" + file);
		}
	}

	public static void main(String[] args) {
		// String targetPath = "D:\\旧文档";
		// File file = ZipUtil.zip(targetPath);
		// System.out.println(file);
		// ZipUtil.unzip("F:\\Win7壁纸.zip");
		// String file1 = "E:/DevFolder/workspaces/ws64bit/java-base-learning/src/main/resources/pmml_model_保险.pmml";

		List<File> fileList = Arrays.asList(//
				new File("E:/DevFolder/workspaces/ws64bit/java-base-learning/src/main/resources/pmml_model_保险.pmml"), //
				new File("E:/DevFolder/workspaces/ws64bit/java-base-learning/src/main/resources/pmml_model_健康.pmml"), //
				new File("D:/测试打包") //
		//
		);
		String zipPath = "D:/myzip.zip";

		File out = genZipFile(fileList, zipPath);
		System.out.println(out);
	}
}