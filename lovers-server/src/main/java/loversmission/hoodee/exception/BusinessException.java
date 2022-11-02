package loversmission.hoodee.exception;

/**
 * @PackageName: loversmission.hoodee.exception
 * @ClassName: FactorySaasBusinessException
 * @Description:
 * @author: zhuguangxun
 * @date 2020/11/28
 */
public class BusinessException extends RuntimeException {

    private Integer code;

    private String errMsg;

    public BusinessException(Integer code, String errMsg) {
        super(errMsg);
        this.code = code;
        this.errMsg = errMsg;
    }

    public BusinessException(String message, Integer code, String errMsg) {
        super(message);
        this.code = code;
        this.errMsg = errMsg;
    }

    public BusinessException(String message, Throwable cause, Integer code, String errMsg) {
        super(message, cause);
        this.code = code;
        this.errMsg = errMsg;
    }

    public BusinessException(Throwable cause, Integer code, String errMsg) {
        super(cause);
        this.code = code;
        this.errMsg = errMsg;
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer code, String errMsg) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.errMsg = errMsg;
    }

    public Integer getCode() {
        return code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public static BusinessException newBusinessException(int code, String msg) {
        throw new BusinessException(code, msg);
    }
}
