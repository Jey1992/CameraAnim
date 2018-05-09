package cn.jey1016.cameraanim.bean;

/**
 * EventBus基础数据模型
 */
public class BusEvent {
    /**
     * 1.拍照完成
     * 2.本地图片传给相机
     */
    private int code;
    private String message;
    private Object data;

    public BusEvent(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BusEvent(int code, Object data) {
        this.code = code;
        this.data = data;
    }

    public BusEvent(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
