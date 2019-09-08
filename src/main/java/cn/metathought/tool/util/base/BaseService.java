package cn.metathought.tool.util.base;

import java.util.List;

/**
 * @author zhoubinglong
 */
public interface BaseService<T extends BaseEntity, E extends BaseExample> {

    /**
     * 主键id查询
     */
    T selectByPrimaryKey(String id);

    /**
     * 高级查询
     */
    List<T> selectByExample(E example);

    /**
     * 高级查询
     */
    T selectOneByExample(E example);

    /**
     * 查询数量
     */
    long countByExample(E example);

    /**
     * 插入对象
     *
     * @param record 对象
     */
    int insert(T record);

    /**
     * 更新对象
     *
     * @param record 对象
     */
    int updateByPrimaryKey(T record);

    int updateByPrimaryKeySelective(T record);

    int updateByExample(T record, E example);

    int updateByExampleSelective(T record, E example);

    int delete(String id);

    int deleteHard(String id);
}