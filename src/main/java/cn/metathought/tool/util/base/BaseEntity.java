package cn.metathought.tool.util.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 实体类基类
 *
 * @author zhoubinglong
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String id;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date modifyTime;

    /**
     * 创建人
     */
    protected String createUser;

    /**
     * 修改人
     */
    protected String modifyUser;

    /**
     * 删除标志 已删除：Y 未删除：N
     */
    protected String isDeleted;

    protected Integer pageNum = 1;
    protected Integer pageSize = Integer.MAX_VALUE;

}
