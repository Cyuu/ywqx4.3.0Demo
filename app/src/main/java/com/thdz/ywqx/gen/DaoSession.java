package com.thdz.ywqx.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.thdz.ywqx.bean.MonitorBean;
import com.thdz.ywqx.bean.StateCodeBean;

import com.thdz.ywqx.gen.MonitorBeanDao;
import com.thdz.ywqx.gen.StateCodeBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig monitorBeanDaoConfig;
    private final DaoConfig stateCodeBeanDaoConfig;

    private final MonitorBeanDao monitorBeanDao;
    private final StateCodeBeanDao stateCodeBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        monitorBeanDaoConfig = daoConfigMap.get(MonitorBeanDao.class).clone();
        monitorBeanDaoConfig.initIdentityScope(type);

        stateCodeBeanDaoConfig = daoConfigMap.get(StateCodeBeanDao.class).clone();
        stateCodeBeanDaoConfig.initIdentityScope(type);

        monitorBeanDao = new MonitorBeanDao(monitorBeanDaoConfig, this);
        stateCodeBeanDao = new StateCodeBeanDao(stateCodeBeanDaoConfig, this);

        registerDao(MonitorBean.class, monitorBeanDao);
        registerDao(StateCodeBean.class, stateCodeBeanDao);
    }
    
    public void clear() {
        monitorBeanDaoConfig.clearIdentityScope();
        stateCodeBeanDaoConfig.clearIdentityScope();
    }

    public MonitorBeanDao getMonitorBeanDao() {
        return monitorBeanDao;
    }

    public StateCodeBeanDao getStateCodeBeanDao() {
        return stateCodeBeanDao;
    }

}
