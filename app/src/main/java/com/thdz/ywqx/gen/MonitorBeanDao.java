package com.thdz.ywqx.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.thdz.ywqx.bean.MonitorBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MONITOR_BEAN".
*/
public class MonitorBeanDao extends AbstractDao<MonitorBean, Long> {

    public static final String TABLENAME = "MONITOR_BEAN";

    /**
     * Properties of entity MonitorBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UnitId = new Property(1, String.class, "UnitId", false, "UNIT_ID");
        public final static Property UnitNo = new Property(2, String.class, "UnitNo", false, "UNIT_NO");
        public final static Property UnitName = new Property(3, String.class, "UnitName", false, "UNIT_NAME");
        public final static Property FacNo = new Property(4, String.class, "FacNo", false, "FAC_NO");
        public final static Property AlarmCnt = new Property(5, String.class, "AlarmCnt", false, "ALARM_CNT");
        public final static Property AlarmCanCnt = new Property(6, String.class, "AlarmCanCnt", false, "ALARM_CAN_CNT");
        public final static Property EnableState = new Property(7, String.class, "EnableState", false, "ENABLE_STATE");
        public final static Property RBId = new Property(8, String.class, "RBId", false, "RBID");
        public final static Property RBNo = new Property(9, String.class, "RBNo", false, "RBNO");
        public final static Property RBName = new Property(10, String.class, "RBName", false, "RBNAME");
        public final static Property RSId = new Property(11, String.class, "RSId", false, "RSID");
        public final static Property RSName = new Property(12, String.class, "RSName", false, "RSNAME");
        public final static Property RWIId = new Property(13, String.class, "RWIId", false, "RWIID");
        public final static Property RWIName = new Property(14, String.class, "RWIName", false, "RWINAME");
        public final static Property StnId = new Property(15, String.class, "StnId", false, "STN_ID");
        public final static Property StnNo = new Property(16, String.class, "StnNo", false, "STN_NO");
        public final static Property StnName = new Property(17, String.class, "StnName", false, "STN_NAME");
        public final static Property PcdtId = new Property(18, String.class, "PcdtId", false, "PCDT_ID");
        public final static Property PcdtSid = new Property(19, String.class, "PcdtSid", false, "PCDT_SID");
        public final static Property PcdtName = new Property(20, String.class, "PcdtName", false, "PCDT_NAME");
        public final static Property RLId = new Property(21, String.class, "RLId", false, "RLID");
        public final static Property RLName = new Property(22, String.class, "RLName", false, "RLNAME");
        public final static Property RLRowType = new Property(23, String.class, "RLRowType", false, "RLROW_TYPE");
        public final static Property AppId = new Property(24, String.class, "AppId", false, "APP_ID");
        public final static Property AppName = new Property(25, String.class, "AppName", false, "APP_NAME");
    }


    public MonitorBeanDao(DaoConfig config) {
        super(config);
    }
    
    public MonitorBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MONITOR_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"UNIT_ID\" TEXT UNIQUE ," + // 1: UnitId
                "\"UNIT_NO\" TEXT," + // 2: UnitNo
                "\"UNIT_NAME\" TEXT," + // 3: UnitName
                "\"FAC_NO\" TEXT," + // 4: FacNo
                "\"ALARM_CNT\" TEXT," + // 5: AlarmCnt
                "\"ALARM_CAN_CNT\" TEXT," + // 6: AlarmCanCnt
                "\"ENABLE_STATE\" TEXT," + // 7: EnableState
                "\"RBID\" TEXT," + // 8: RBId
                "\"RBNO\" TEXT," + // 9: RBNo
                "\"RBNAME\" TEXT," + // 10: RBName
                "\"RSID\" TEXT," + // 11: RSId
                "\"RSNAME\" TEXT," + // 12: RSName
                "\"RWIID\" TEXT," + // 13: RWIId
                "\"RWINAME\" TEXT," + // 14: RWIName
                "\"STN_ID\" TEXT," + // 15: StnId
                "\"STN_NO\" TEXT," + // 16: StnNo
                "\"STN_NAME\" TEXT," + // 17: StnName
                "\"PCDT_ID\" TEXT," + // 18: PcdtId
                "\"PCDT_SID\" TEXT," + // 19: PcdtSid
                "\"PCDT_NAME\" TEXT," + // 20: PcdtName
                "\"RLID\" TEXT," + // 21: RLId
                "\"RLNAME\" TEXT," + // 22: RLName
                "\"RLROW_TYPE\" TEXT," + // 23: RLRowType
                "\"APP_ID\" TEXT," + // 24: AppId
                "\"APP_NAME\" TEXT);"); // 25: AppName
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MONITOR_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MonitorBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String UnitId = entity.getUnitId();
        if (UnitId != null) {
            stmt.bindString(2, UnitId);
        }
 
        String UnitNo = entity.getUnitNo();
        if (UnitNo != null) {
            stmt.bindString(3, UnitNo);
        }
 
        String UnitName = entity.getUnitName();
        if (UnitName != null) {
            stmt.bindString(4, UnitName);
        }
 
        String FacNo = entity.getFacNo();
        if (FacNo != null) {
            stmt.bindString(5, FacNo);
        }
 
        String AlarmCnt = entity.getAlarmCnt();
        if (AlarmCnt != null) {
            stmt.bindString(6, AlarmCnt);
        }
 
        String AlarmCanCnt = entity.getAlarmCanCnt();
        if (AlarmCanCnt != null) {
            stmt.bindString(7, AlarmCanCnt);
        }
 
        String EnableState = entity.getEnableState();
        if (EnableState != null) {
            stmt.bindString(8, EnableState);
        }
 
        String RBId = entity.getRBId();
        if (RBId != null) {
            stmt.bindString(9, RBId);
        }
 
        String RBNo = entity.getRBNo();
        if (RBNo != null) {
            stmt.bindString(10, RBNo);
        }
 
        String RBName = entity.getRBName();
        if (RBName != null) {
            stmt.bindString(11, RBName);
        }
 
        String RSId = entity.getRSId();
        if (RSId != null) {
            stmt.bindString(12, RSId);
        }
 
        String RSName = entity.getRSName();
        if (RSName != null) {
            stmt.bindString(13, RSName);
        }
 
        String RWIId = entity.getRWIId();
        if (RWIId != null) {
            stmt.bindString(14, RWIId);
        }
 
        String RWIName = entity.getRWIName();
        if (RWIName != null) {
            stmt.bindString(15, RWIName);
        }
 
        String StnId = entity.getStnId();
        if (StnId != null) {
            stmt.bindString(16, StnId);
        }
 
        String StnNo = entity.getStnNo();
        if (StnNo != null) {
            stmt.bindString(17, StnNo);
        }
 
        String StnName = entity.getStnName();
        if (StnName != null) {
            stmt.bindString(18, StnName);
        }
 
        String PcdtId = entity.getPcdtId();
        if (PcdtId != null) {
            stmt.bindString(19, PcdtId);
        }
 
        String PcdtSid = entity.getPcdtSid();
        if (PcdtSid != null) {
            stmt.bindString(20, PcdtSid);
        }
 
        String PcdtName = entity.getPcdtName();
        if (PcdtName != null) {
            stmt.bindString(21, PcdtName);
        }
 
        String RLId = entity.getRLId();
        if (RLId != null) {
            stmt.bindString(22, RLId);
        }
 
        String RLName = entity.getRLName();
        if (RLName != null) {
            stmt.bindString(23, RLName);
        }
 
        String RLRowType = entity.getRLRowType();
        if (RLRowType != null) {
            stmt.bindString(24, RLRowType);
        }
 
        String AppId = entity.getAppId();
        if (AppId != null) {
            stmt.bindString(25, AppId);
        }
 
        String AppName = entity.getAppName();
        if (AppName != null) {
            stmt.bindString(26, AppName);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MonitorBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String UnitId = entity.getUnitId();
        if (UnitId != null) {
            stmt.bindString(2, UnitId);
        }
 
        String UnitNo = entity.getUnitNo();
        if (UnitNo != null) {
            stmt.bindString(3, UnitNo);
        }
 
        String UnitName = entity.getUnitName();
        if (UnitName != null) {
            stmt.bindString(4, UnitName);
        }
 
        String FacNo = entity.getFacNo();
        if (FacNo != null) {
            stmt.bindString(5, FacNo);
        }
 
        String AlarmCnt = entity.getAlarmCnt();
        if (AlarmCnt != null) {
            stmt.bindString(6, AlarmCnt);
        }
 
        String AlarmCanCnt = entity.getAlarmCanCnt();
        if (AlarmCanCnt != null) {
            stmt.bindString(7, AlarmCanCnt);
        }
 
        String EnableState = entity.getEnableState();
        if (EnableState != null) {
            stmt.bindString(8, EnableState);
        }
 
        String RBId = entity.getRBId();
        if (RBId != null) {
            stmt.bindString(9, RBId);
        }
 
        String RBNo = entity.getRBNo();
        if (RBNo != null) {
            stmt.bindString(10, RBNo);
        }
 
        String RBName = entity.getRBName();
        if (RBName != null) {
            stmt.bindString(11, RBName);
        }
 
        String RSId = entity.getRSId();
        if (RSId != null) {
            stmt.bindString(12, RSId);
        }
 
        String RSName = entity.getRSName();
        if (RSName != null) {
            stmt.bindString(13, RSName);
        }
 
        String RWIId = entity.getRWIId();
        if (RWIId != null) {
            stmt.bindString(14, RWIId);
        }
 
        String RWIName = entity.getRWIName();
        if (RWIName != null) {
            stmt.bindString(15, RWIName);
        }
 
        String StnId = entity.getStnId();
        if (StnId != null) {
            stmt.bindString(16, StnId);
        }
 
        String StnNo = entity.getStnNo();
        if (StnNo != null) {
            stmt.bindString(17, StnNo);
        }
 
        String StnName = entity.getStnName();
        if (StnName != null) {
            stmt.bindString(18, StnName);
        }
 
        String PcdtId = entity.getPcdtId();
        if (PcdtId != null) {
            stmt.bindString(19, PcdtId);
        }
 
        String PcdtSid = entity.getPcdtSid();
        if (PcdtSid != null) {
            stmt.bindString(20, PcdtSid);
        }
 
        String PcdtName = entity.getPcdtName();
        if (PcdtName != null) {
            stmt.bindString(21, PcdtName);
        }
 
        String RLId = entity.getRLId();
        if (RLId != null) {
            stmt.bindString(22, RLId);
        }
 
        String RLName = entity.getRLName();
        if (RLName != null) {
            stmt.bindString(23, RLName);
        }
 
        String RLRowType = entity.getRLRowType();
        if (RLRowType != null) {
            stmt.bindString(24, RLRowType);
        }
 
        String AppId = entity.getAppId();
        if (AppId != null) {
            stmt.bindString(25, AppId);
        }
 
        String AppName = entity.getAppName();
        if (AppName != null) {
            stmt.bindString(26, AppName);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public MonitorBean readEntity(Cursor cursor, int offset) {
        MonitorBean entity = new MonitorBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // UnitId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // UnitNo
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // UnitName
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // FacNo
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // AlarmCnt
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // AlarmCanCnt
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // EnableState
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // RBId
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // RBNo
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // RBName
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // RSId
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // RSName
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // RWIId
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // RWIName
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // StnId
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // StnNo
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // StnName
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // PcdtId
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // PcdtSid
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // PcdtName
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // RLId
            cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22), // RLName
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // RLRowType
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // AppId
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25) // AppName
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MonitorBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUnitId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUnitNo(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUnitName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setFacNo(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setAlarmCnt(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setAlarmCanCnt(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setEnableState(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setRBId(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setRBNo(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setRBName(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setRSId(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setRSName(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setRWIId(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setRWIName(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setStnId(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setStnNo(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setStnName(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setPcdtId(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setPcdtSid(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setPcdtName(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setRLId(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setRLName(cursor.isNull(offset + 22) ? null : cursor.getString(offset + 22));
        entity.setRLRowType(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setAppId(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setAppName(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(MonitorBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(MonitorBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MonitorBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}