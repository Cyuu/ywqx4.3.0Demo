package com.thdz.ywqx.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.thdz.ywqx.app.MyApplication;
import com.thdz.ywqx.bean.DeptBean;
import com.thdz.ywqx.bean.MonitorBean;
import com.thdz.ywqx.bean.StationBean;
import com.thdz.ywqx.bean.UnitBean;
import com.thdz.ywqx.gen.DaoMaster;
import com.thdz.ywqx.gen.DaoSession;
import com.thdz.ywqx.gen.MonitorBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * GreenDao数据库管理
 */

public class DBManager {

    private final static String dbName = "ywqx_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;

    public DBManager() {
        openHelper = new DaoMaster.DevOpenHelper(MyApplication.getApplication(), dbName, null);
    }

    /**
     * 获取单例引用
     */
    public static DBManager getInstance() {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(MyApplication.getApplication(), dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(MyApplication.getApplication(), dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

//    /**
//     * 插入一条记录
//     *
//     * @param UserBean
//     */
//    public void insertUserBean(UserBean UserBean) {
//        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        UserBeanDao UserBeanDao = daoSession.getUserBeanDao();
//        UserBeanDao.insert(UserBean);
//    }
//
//    /**
//     * 插入用户集合
//     *
//     * @param UserBeans
//     */
//    public void insertUserBeanList(List<UserBean> UserBeans) {
//        if (UserBeans == null || UserBeans.isEmpty()) {
//            return;
//        }
//        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        UserBeanDao UserBeanDao = daoSession.getUserBeanDao();
//        UserBeanDao.insertInTx(UserBeans);
//    }
//
//    /**
//     * 删除一条记录
//     *
//     * @param UserBean
//     */
//    public void deleteUserBean(UserBean UserBean) {
//        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        UserBeanDao UserBeanDao = daoSession.getUserBeanDao();
//        UserBeanDao.delete(UserBean);
//    }
//
//    /**
//     * 更新一条记录
//     *
//     * @param UserBean
//     */
//    public void updateUserBean(UserBean UserBean) {
//        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        UserBeanDao UserBeanDao = daoSession.getUserBeanDao();
//        UserBeanDao.update(UserBean);
//    }
//
//    /**
//     * 查询用户列表
//     */
//    public List<UserBean> queryUserBeanList() {
//        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        UserBeanDao UserBeanDao = daoSession.getUserBeanDao();
//        QueryBuilder<UserBean> qb = UserBeanDao.queryBuilder();
//        List<UserBean> list = qb.list();
//        return list;
//    }
//
//    /**
//     * 查询用户列表
//     */
//    public List<UserBean> queryUserBeanList(int age) {
//        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        UserBeanDao UserBeanDao = daoSession.getUserBeanDao();
//        QueryBuilder<UserBean> qb = UserBeanDao.queryBuilder();
////        qb.where(UserBeanDao.Properties.Age.gt(age)).orderAsc(UserBeanDao.Properties.Age);
//        List<UserBean> list = qb.list();
//        return list;
//    }

    /**
     * 插入监控点列表
     */
    public void insertMonitorBeanList(List<MonitorBean> monitorBeans) {
        try {
            if (monitorBeans == null || monitorBeans.isEmpty()) {
                return;
            }
            DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
            DaoSession daoSession = daoMaster.newSession();
            MonitorBeanDao moniDao = daoSession.getMonitorBeanDao();
            moniDao.deleteAll();
            moniDao.insertInTx(monitorBeans);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询监控点列表
     */
    public List<MonitorBean> queryMonitorBeanList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        MonitorBeanDao moniDao = daoSession.getMonitorBeanDao();
        QueryBuilder<MonitorBean> qb = moniDao.queryBuilder();
        List<MonitorBean> list = qb.list();
        return list;
    }

    /**
     * 查询监控点 -- 局列表
     */
    public List<DeptBean> queryDeptBeanList() {
        List<DeptBean> list = new ArrayList<>();
        DeptBean bean = null;
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
//        MonitorBeanDao moniDao = daoSession.getMonitorBeanDao();
//        QueryBuilder<MonitorBean> qb = moniDao.queryBuilder();
//        List<MonitorBean> list = qb.list();

        Cursor c = daoSession.getDatabase().rawQuery(SQL_DEPT_LIST, null);
        try {
            if (c.moveToFirst()) {
                do {
                    bean = new DeptBean();
                    bean.setRBId(c.getString(0));
                    bean.setRBName(c.getString(1));
                    bean.setRBNo(c.getString(2));
                    list.add(bean);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }

        return list;
    }

    private static final String SQL_DEPT_LIST = "SELECT DISTINCT "
            + MonitorBeanDao.Properties.RBId.columnName + ","
            + MonitorBeanDao.Properties.RBName.columnName + ","
            + MonitorBeanDao.Properties.RBNo.columnName + ""
            + " FROM " + MonitorBeanDao.TABLENAME;


    /**
     * 根据StnNO查询，对应的StnName
     */
    public String getStnNameByStnNo(String StnNo) {
        try {
            DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
            DaoSession daoSession = daoMaster.newSession();
            MonitorBeanDao StnDao = daoSession.getMonitorBeanDao();
            QueryBuilder<MonitorBean> qBuilder = StnDao.queryBuilder();
            String name = qBuilder.where(MonitorBeanDao.Properties.StnNo.eq(StnNo)).build().list().get(0).getStnName();
            return name;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }


    /**
     * 查询监控点 -- 局列表
     */
    public List<DeptBean> queryStationBeanList() {
        List<DeptBean> list = new ArrayList<>();
        DeptBean bean = null;
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();

        Cursor c = daoSession.getDatabase().rawQuery(SQL_DEPT_LIST, null);
        try {
            if (c.moveToFirst()) {
                do {
                    bean = new DeptBean();
                    bean.setRBId(c.getString(0));
                    bean.setRBName(c.getString(1));
                    bean.setRBNo(c.getString(2));
                    list.add(bean);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }

        return list;
    }


    /**
     * 局信息检索
     */
    public List<StationBean> getStationListByKeyword(String keyword) {
        try {
            List<StationBean> stnList = new ArrayList<StationBean>();
            List<String> stnIdList = new ArrayList<String>();
            DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
            DaoSession daoSession = daoMaster.newSession();
            MonitorBeanDao monitorDao = daoSession.getMonitorBeanDao();
            QueryBuilder<MonitorBean> qBuilder = monitorDao.queryBuilder();
            List<MonitorBean> monitorList = qBuilder.whereOr(
                    MonitorBeanDao.Properties.StnId.eq(keyword),
                    MonitorBeanDao.Properties.StnNo.eq(keyword),
                    MonitorBeanDao.Properties.StnName.eq(keyword)
            ).build().list();

            StationBean stnBean = null;
            for (MonitorBean bean : monitorList) {
                if (!stnIdList.contains(bean.getStnId())) {
                    stnIdList.add(bean.getStnId());

                    stnBean = new StationBean();
                    stnBean.setRSId(bean.getRSId());
                    stnBean.setRWIId(bean.getRWIId());
                    stnBean.setRBId(bean.getRBId());
                    stnBean.setRLId(bean.getRLId());
                    stnBean.setRLName(bean.getRLName());
                    stnBean.setStnId(bean.getStnId());
                    stnBean.setStnName(bean.getStnName());
                    stnBean.setStnNo(bean.getStnNo());

                    stnList.add(stnBean);

                }
            }

            return stnList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 站点信息检索 - 查询数据库里所有符合条件的站点列表
     */
    public List<UnitBean> getDeptListByKeyword(String keyword) {

        return null;
    }


//    /**
//     * 插入StateCode列表<br/>
//     * TODO 需要先判断是否存在
//     */
//    public void insertStateCodeList(List<StateCodeBean> codeBeans) {
//        if (codeBeans == null || codeBeans.isEmpty()) {
//            return;
//        }
//        try {
//            DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
//            DaoSession daoSession = daoMaster.newSession();
//            StateCodeBeanDao codeDao = daoSession.getStateCodeBeanDao();
//
//            // 先清空表
//            codeDao.deleteAll();
//            // 再插入
//            codeDao.insertInTx(codeBeans);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * 根据code查询，对应的codename和codevalue
//     */
//    public String getNameValueByCode(String code){
//        try {
//            StateCodeBean bean = null;
//            DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
//            DaoSession daoSession = daoMaster.newSession();
//            StateCodeBeanDao codeDao = daoSession.getStateCodeBeanDao();
//            QueryBuilder<StateCodeBean> qBuilder = codeDao.queryBuilder();
//            List<StateCodeBean> codeList = qBuilder.where(StateCodeBeanDao.Properties.CodeId.eq(code)).build().list();
//            if (codeList != null && !codeList.isEmpty()) {
//                bean = codeList.get(0);
//                return bean.getCodeName()+":"+bean.getCodeValue();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//
//    }
//
//    /**
//     * 根据code查询，对应的codename -- 用于展示状态的title
//     */
//    public String getCodeNameByCode(String code){
//        try {
//            StateCodeBean bean = null;
//            DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
//            DaoSession daoSession = daoMaster.newSession();
//            StateCodeBeanDao codeDao = daoSession.getStateCodeBeanDao();
//            QueryBuilder<StateCodeBean> qBuilder = codeDao.queryBuilder();
//            List<StateCodeBean> codeList = qBuilder.where(StateCodeBeanDao.Properties.CodeId.eq(code)).build().list();
//            if (codeList != null && !codeList.isEmpty()) {
//                bean = codeList.get(0);
//                return bean.getCodeName();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//
//    }
//
//    /**
//     * 根据code查询，对应的codevalue -- 用于展示状态的value
//     */
//    public String getCodeValueByCode(String code){
//        try {
//            StateCodeBean bean = null;
//            DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
//            DaoSession daoSession = daoMaster.newSession();
//            StateCodeBeanDao codeDao = daoSession.getStateCodeBeanDao();
//            QueryBuilder<StateCodeBean> qBuilder = codeDao.queryBuilder();
//            List<StateCodeBean> codeList = qBuilder.where(StateCodeBeanDao.Properties.CodeId.eq(code)).build().list();
//            if (codeList != null && !codeList.isEmpty()) {
//                bean = codeList.get(0);
//                return bean.getCodeValue();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//
//    }
//
//
//
//    /**
//     * 根据code查询，对应的codename和codevalue
//     */
//    public String getCodeStringByCode(String code){
//        try {
//            StateCodeBean bean = null;
//            DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
//            DaoSession daoSession = daoMaster.newSession();
//            StateCodeBeanDao codeDao = daoSession.getStateCodeBeanDao();
//            QueryBuilder<StateCodeBean> qBuilder = codeDao.queryBuilder();
//            List<StateCodeBean> codeList = qBuilder.where(StateCodeBeanDao.Properties.CodeId.eq(code)).build().list();
//            if (codeList != null && !codeList.isEmpty()) {
//                bean = codeList.get(0);
//                return bean.getCodeString();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//
//    }
//
//
//    /**
//     * 根据UnitNo查询，对应的UnitName
//     */
//    public String getUnitNameByUnitNo(String UnitNo){
//        try {
//            MonitorBean bean = null;
//            DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
//            DaoSession daoSession = daoMaster.newSession();
//            MonitorBeanDao StnDao = daoSession.getMonitorBeanDao();
//            QueryBuilder<MonitorBean> qBuilder = StnDao.queryBuilder();
//            String name = qBuilder.where(MonitorBeanDao.Properties.UnitNo.eq(UnitNo)).build().list().get(0).getUnitName();
//            return name;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//
//    }
//
//
//
//    /**
//     * 根据code查询，对应的GroupId
//     */
//    public String getGroupIdByCode(String code){
//        try {
//            StateCodeBean bean = null;
//            DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
//            DaoSession daoSession = daoMaster.newSession();
//            StateCodeBeanDao codeDao = daoSession.getStateCodeBeanDao();
//            QueryBuilder<StateCodeBean> qBuilder = codeDao.queryBuilder();
//            List<StateCodeBean> codeList = qBuilder.where(StateCodeBeanDao.Properties.CodeId.eq(code)).build().list();
//            if (codeList != null && !codeList.isEmpty()) {
//                bean = codeList.get(0);
//                return bean.getGroupId();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "-1";
//
//    }
//
//
//    /**
//     * 查询监控点列表
//     */
//    public List<MonitorBean> queryMonitorBeanList() {
//        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        MonitorBeanDao moniDao = daoSession.getMonitorBeanDao();
//        QueryBuilder<MonitorBean> qb = moniDao.queryBuilder();
//        List<MonitorBean> list = qb.list();
//        return list;
//    }


//    /**
//     * 根据code查询，对应的codename和codevalue  没什么用，测试的
//     */
//    public List<StateCodeBean> getAllStateCodeList(){
//        try {
//            StateCodeBean bean = null;
//            DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
//            DaoSession daoSession = daoMaster.newSession();
//            StateCodeBeanDao codeDao = daoSession.getStateCodeBeanDao();
//            QueryBuilder<StateCodeBean> qBuilder = codeDao.queryBuilder();
//            List<StateCodeBean> list = qBuilder.build().list();
//            return list;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//    }

}