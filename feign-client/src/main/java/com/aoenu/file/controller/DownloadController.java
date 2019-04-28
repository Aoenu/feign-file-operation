package com.aoenu.file.controller;

import com.aoenu.file.utils.ZipUtil;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipOutputStream;

/**
 * 展示了文件下载的功能。<br>
 * 
 * 文件下载本质是服务端把文件流通过网络传输到客户端进行保存。 <br>
 * 浏览器是怎么识别这是需要保存文件而不是渲染内容? 服务端通过发送响应头给浏览器，让其知道流是要展示或保存。
 * 
 * @author Stone
 *
 */
@RestController
public class DownloadController {
	/**
	 * 下载文件。从本地中读取文件流模拟
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 * @author Stone
	 */
	@ResponseBody
	@RequestMapping(value = "/download/downloadfile")
	public void downloadFile(//
                             HttpServletRequest req, //
                             HttpServletResponse resp//
	) throws IOException {
		ServletOutputStream out = null;
		FileInputStream fis = null;
		try {
			// STEP 1：模拟得到文件流
			String[] files = { "/testdownloadfile/测试-文本.txt", "/testdownloadfile/测试-图片.jpg" };
			String oriFilenameToBeDownloaded = files[new Random().nextInt(2)];
			String oriFullFilename = this.getClass().getResource(oriFilenameToBeDownloaded).getFile();
			fis = new FileInputStream(oriFullFilename);
			int available = fis.available();// 文件多少字节
			byte[] fileBinaryBytes = new byte[available];
			fis.read(fileBinaryBytes);

			// STEP 2：下载
			// 2.1 获得点击下载后的文件名
			String downloadFilename = "";
			try {
				int lastIndexOfSlash = oriFullFilename.lastIndexOf("\\") > 0 ? oriFullFilename.lastIndexOf("\\")
						: oriFullFilename.lastIndexOf("/");
				downloadFilename = oriFullFilename.substring(lastIndexOfSlash + 1);
				downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");// 如无则不能正确展示下载的文件的文件名
			} catch (Exception e) {
				downloadFilename = "default_filename";
			}
			

			// 2.2 设置响应头。告诉浏览器这是文件流，需要弹框保存文件，并且告诉浏览器填充缺省的文件名
			resp.setContentType("application/octet-stream; charset=UTF-8");
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFilename + "\""); // 设定输出文件头

			// 2.3 获得响应的打印流，打印并flush
			out = resp.getOutputStream();// 注：不能用PrintWriter这种字符流
			out.write(fileBinaryBytes);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}
			// 思考：这里必须关吗?不关会怎么样?SpringMVC框架有关吗?自己不关SpringMVC会帮忙关吗?
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

	}

	/**
	 * 把文件打包成zip，下载这个zip包。这个zip包是不临时写到磁盘再读取其文件流，没有"中转"步骤，所以称之为空中的zip，即air zip。
	 * 
	 * @param req
	 * @param resp
	 * @author Stone
	 * @throws IOException 
	 */
	@ResponseBody
	@RequestMapping(value = "/download/downloadairzip")
	public void downloadAirZip(//
                               HttpServletRequest req, //
                               HttpServletResponse resp//
	) throws IOException {
		ServletOutputStream out = null;
		try {
			// STEP 1：得到要打包的文件
			String[] files = { "/测试-文本.txt", "/测试-图片.jpg", "/测试打包文件夹" };
			List<File> srcPathList = new ArrayList<>();
			for (String file : files) {
				String oriFullFilename = ResourceUtils.getURL("classpath:testdownloadfile").getFile();
				srcPathList.add(new File(oriFullFilename));
			}
			
			
			// STEP 2：指定下载文件名
			String downloadFilename = "空中zip包（该文件不在服务端磁盘生成）.zip";
			downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");// 如无则不能正确展示下载的文件的文件名
			resp.setContentType("application/octet-stream; charset=UTF-8");
			resp.setHeader("Content-Disposition", "attachment; filename=\"" + downloadFilename + "\""); // 设定输出文件头

//			resp.setHeader("Content-Disposition", "attachment; filename=\"" + new String(downloadFilename.getBytes("UTF-8"),"iso-8859-1") + "\"");

			// STEP 3：刷送流出去
			out = resp.getOutputStream();
			ZipOutputStream zos = new ZipOutputStream(out);
			ZipUtil.addEntry(srcPathList, zos);// 该行代码断点，可见执行之后浏览器立刻弹出下载框。可见该方法是立刻把流刷出去了。
			
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			// 思考：这里必须关吗?不关会怎么样?SpringMVC框架有关吗?自己不关SpringMVC会帮忙关吗?
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
		
	}
}