package cn.metathought.tool.util.base;

import java.io.Serializable;
import lombok.Data;

/**
 * example基类
 *
 * @author zhoubinglong
 */
@Data
public class BaseExample implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Integer pageNum = 1;
    protected Integer pageSize = Integer.MAX_VALUE;
}