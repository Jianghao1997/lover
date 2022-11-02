package loversmission.hoodee.common;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.DecimalFormat;

/**
 * Description：公共工具类
 * Author: 蒋豪
 * Date:  2021.04.22 12:33
 * Modified By:
 */
@Component
public class CommonUtils {
    /**
     * 判断文件大小
     *
     * @param len  文件长度
     * @param size 限制大小
     * @param unit 限制单位（B,K,M,G）
     * @return
     */
    public static boolean checkFileSize(Long len, int size, String unit) {
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize > size) {
            return false;
        }
        return true;
    }
    /**
     * 判断文件大小
     *
     * @param len
     *            文件长度
     * @param unit
     *            转换单位（B,K,M,G）
     * @return
     */
    public static String formatFileSize(Long len, String unit) {
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("KB".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(fileSize) + unit;
    }

    /**
     * 封面上传后，删除数据时将封面删除
     * @param path
     */
    public static void deleteUploadFile(String fileSavePath, String path) {
        String filePath = null;
        if(path.contains("images")){
            filePath = fileSavePath + path.substring(path.indexOf("images"));
        }
        if(path.contains("Scenes")){
            filePath = fileSavePath + path.substring(path.indexOf("Scenes"));
        }
        if(path.contains("other")){
            filePath = fileSavePath + path.substring(path.indexOf("other"));
        }
        if(ObjectUtil.isNotNull(filePath)){
            File file = new File(filePath);
            if (file.exists()){
                file.delete();
            }
        }
    }

    /**
     * 公共查询某个字段值是否存在
     * @param mapper mapper
     * @param column 字段名
     * @param columnValue 值
     * @param <T>
     * @return
     */
    public static<T> T dataIsExist(BaseMapper<T> mapper,String column, String columnValue){
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(column,columnValue);
        T data = mapper.selectOne(queryWrapper);
        if (ObjectUtil.isNotNull(data)){
            return data;
        }
        return null;
    }

    public static String getRemoteIP() throws Exception {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = "";
        if (request.getHeader("x-forwarded-for") == null) {
            ip = request.getRemoteAddr();
        }else{
            ip = request.getHeader("x-forwarded-for");
        }
        return ip;
    }
}
