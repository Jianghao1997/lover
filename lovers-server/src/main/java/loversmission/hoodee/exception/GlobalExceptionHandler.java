package loversmission.hoodee.exception;

import loversmission.hoodee.common.Result;
import loversmission.hoodee.enums.SystemCodeEnum;
import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;

/**
 * 全局异常处理器
 *
 * @author ruoyi
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        e.printStackTrace();
        String ermsg = e.getMessage();
        if (ermsg != null){
            if (ermsg.contains("Broken pipe") || e instanceof ClientAbortException){
                logger.warn("全局异常，异常信息为：{}", e.getMessage());
                return Result.error(SystemCodeEnum.EXCEPTION,"连接异常");
            }
        }
        logger.warn("全局异常，异常信息为：{}", e.getMessage());
        String message = null;
        if(e instanceof TooManyResultsException){
            message = "数据异常：结果集超过一行记录";
        }else if(e.getCause() instanceof SQLIntegrityConstraintViolationException){
            message = "数据异常：添加失败，信息已存在";
        }else if(e instanceof NullPointerException){
            e.printStackTrace();
        }
        //数据库操作异常
        if (e instanceof SQLSyntaxErrorException || e instanceof InvalidDataAccessResourceUsageException){
            message = "数据异常：数据处理失败，请联系管理员";
        }
        if(StringUtils.isEmpty(message)){
            message = "操作异常，数据处理失败，请联系管理员";
        }
        return Result.error(SystemCodeEnum.EXCEPTION, message);
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> validExceptionHandler(MethodArgumentNotValidException e) {
        logger.warn("全局异常，异常信息为：{}", e.getMessage());
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Result.error(message);
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<String> validExceptionHandler(RuntimeException e) {
        e.printStackTrace();
        logger.warn("全局异常，异常信息为：{}", e.getMessage());
        String message = e.getMessage();
        return Result.error(message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> argumentException(IllegalArgumentException e) {
        logger.warn("全局异常，异常信息为：{}", e.getMessage());
        return Result.error(SystemCodeEnum.EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result<String> businessException(BusinessException e) {
        e.printStackTrace();
        logger.warn("全局异常，异常信息为：{}", e.getMessage());
        return Result.error(e.getCode(), e.getErrMsg());
    }

}
