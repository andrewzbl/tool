package cn.metathought.tool.util.base;


import java.util.List;

/**
 * dao基类
 *
 * @author zhoubinglong
 */
public interface BaseDao<TEntry extends BaseEntity, TExample extends BaseExample> {

    TEntry selectByPrimaryKey(String id);

    List<TEntry> selectByExample(TExample example);

    long countByExample(TExample example);

    int insert(TEntry record);

    int insertSelective(TEntry record);

    int updateByPrimaryKeySelective(TEntry record);

    int updateByPrimaryKey(TEntry record);

    int updateByExampleSelective(TEntry record, TExample example);

    int updateByExample(TEntry record, TExample example);

    int deleteByExample(TExample example);

    int deleteByPrimaryKey(String id);
}