package cn.metathought.tool.util.base;

import cn.metathought.tool.util.CodeUtil;
import cn.metathought.tool.util.Const;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zhoubinglong
 */
public abstract class BaseServiceImpl<T extends BaseEntity, E extends BaseExample> implements BaseService<T, E> {

    protected abstract BaseDao<T, E> getDao();

    @Override
    public T selectByPrimaryKey(String id) {
        return getDao().selectByPrimaryKey(id);
    }

    @Override
    public List<T> selectByExample(E example) {
        return getDao().selectByExample(example);
    }

    @Override
    public T selectOneByExample(E example) {
        List<T> list = selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public long countByExample(E example) {
        return getDao().countByExample(example);
    }

    @Override
    public int insert(T record) {
        if (StringUtils.isBlank(record.getId())) {
            record.setId(CodeUtil.uuid());
        }
        return getDao().insertSelective(record);
    }

    @Override
    public int updateByPrimaryKey(T record) {
        return getDao().updateByPrimaryKey(record);
    }

    @Override
    public int updateByPrimaryKeySelective(T record) {
        return getDao().updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByExample(T record, E example) {
        return getDao().updateByExample(record, example);
    }

    @Override
    public int updateByExampleSelective(T record, E example) {
        return getDao().updateByExampleSelective(record, example);
    }

    @Override
    public int delete(String id) {
        T entity = selectByPrimaryKey(id);
        entity.setIsDeleted(Const.YES);
        entity.setModifyUser(Const.SYSTEM_USER);
        return updateByPrimaryKeySelective(entity);
    }

    @Override
    public int deleteHard(String id) {
        return getDao().deleteByPrimaryKey(id);
    }
}