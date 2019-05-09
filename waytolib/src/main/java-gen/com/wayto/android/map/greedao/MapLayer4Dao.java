package com.wayto.android.map.greedao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MAP_LAYER4".
*/
public class MapLayer4Dao extends AbstractDao<MapLayer4, Void> {

    public static final String TABLENAME = "MAP_LAYER4";

    /**
     * Properties of entity MapLayer4.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property LayerType = new Property(0, Integer.class, "layerType", false, "LAYER_TYPE");
        public final static Property Level = new Property(1, Integer.class, "level", false, "LEVEL");
        public final static Property Col = new Property(2, Integer.class, "col", false, "COL");
        public final static Property Row = new Property(3, Integer.class, "row", false, "ROW");
        public final static Property Layer = new Property(4, byte[].class, "layer", false, "LAYER");
    }


    public MapLayer4Dao(DaoConfig config) {
        super(config);
    }
    
    public MapLayer4Dao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MAP_LAYER4\" (" + //
                "\"LAYER_TYPE\" INTEGER," + // 0: layerType
                "\"LEVEL\" INTEGER," + // 1: level
                "\"COL\" INTEGER," + // 2: col
                "\"ROW\" INTEGER," + // 3: row
                "\"LAYER\" BLOB);"); // 4: layer
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MAP_LAYER4\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MapLayer4 entity) {
        stmt.clearBindings();
 
        Integer layerType = entity.getLayerType();
        if (layerType != null) {
            stmt.bindLong(1, layerType);
        }
 
        Integer level = entity.getLevel();
        if (level != null) {
            stmt.bindLong(2, level);
        }
 
        Integer col = entity.getCol();
        if (col != null) {
            stmt.bindLong(3, col);
        }
 
        Integer row = entity.getRow();
        if (row != null) {
            stmt.bindLong(4, row);
        }
 
        byte[] layer = entity.getLayer();
        if (layer != null) {
            stmt.bindBlob(5, layer);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MapLayer4 entity) {
        stmt.clearBindings();
 
        Integer layerType = entity.getLayerType();
        if (layerType != null) {
            stmt.bindLong(1, layerType);
        }
 
        Integer level = entity.getLevel();
        if (level != null) {
            stmt.bindLong(2, level);
        }
 
        Integer col = entity.getCol();
        if (col != null) {
            stmt.bindLong(3, col);
        }
 
        Integer row = entity.getRow();
        if (row != null) {
            stmt.bindLong(4, row);
        }
 
        byte[] layer = entity.getLayer();
        if (layer != null) {
            stmt.bindBlob(5, layer);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public MapLayer4 readEntity(Cursor cursor, int offset) {
        MapLayer4 entity = new MapLayer4( //
            cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0), // layerType
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // level
            cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2), // col
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // row
            cursor.isNull(offset + 4) ? null : cursor.getBlob(offset + 4) // layer
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MapLayer4 entity, int offset) {
        entity.setLayerType(cursor.isNull(offset + 0) ? null : cursor.getInt(offset + 0));
        entity.setLevel(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setCol(cursor.isNull(offset + 2) ? null : cursor.getInt(offset + 2));
        entity.setRow(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setLayer(cursor.isNull(offset + 4) ? null : cursor.getBlob(offset + 4));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(MapLayer4 entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(MapLayer4 entity) {
        return null;
    }

    @Override
    public boolean hasKey(MapLayer4 entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
