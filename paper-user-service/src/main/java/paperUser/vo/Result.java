package paperUser.vo;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Data
@Setter
@Getter
public class Result implements Serializable {
    private int code;
    private String message;
    private Object data;


    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static Result success() {
        return new Result(200, "成功");
    }

    public static Result success(Object data) {
        return new Result(200, "成功", data);
    }


}
