package ir.mehdi.kelid.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.HashMap;


import ir.mehdi.kelid.Constant;
import ir.mehdi.kelid.KelidApplication;
import ir.mehdi.kelid.model.Property;
import ir.mehdi.kelid.utils.FileUtils;
import ir.mehdi.kelid.utils.Utils;

/**
 * Created by Iman on 7/28/2016.
 */
public class MySqliteOpenHelper extends SQLiteOpenHelper implements Constant {

    public static Property property;
    final public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final public static SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
    public static final String table_name = "PROPERTY";
    public static final String search_history_table_name = "search_history";
    public static final String table_image_name = "property_image";
    public static final String table_property_detail_name = "property_detail";

    private static MySqliteOpenHelper instance;
    public HashMap<Long, Property> historyPropertys = new HashMap<>();
    public HashMap<Long, Property> loadedPropertys = new HashMap<>();
    public HashMap<Long, Property> myPropertys = new HashMap<>();
    public HashMap<Long, Property> myPropertysremote = new HashMap<>();
    public HashMap<Long, Property> allPropertys = new HashMap<>();
    public HashMap<Long, Property> bookmarkPropertys = new HashMap<>();


    public static MySqliteOpenHelper getInstance() {
        if (instance == null) {
            instance = new MySqliteOpenHelper(KelidApplication.applicationContext);
        }
        return instance;

    }


    public static final String Property_TABLE = "CREATE TABLE " + table_name + " (  local_id integer primary key AUTOINCREMENT," +
            "  remote_id integer,\ncreated DATETIME DEFAULT CURRENT_TIMESTAMP,  bookmark integer," +
            " myproperty integer,name text,send_name text," +
            " title text,send_title text," +
            " desc text,send_desc text, email text,send_email text," +
            " avenue text,send_avenue text, street text,send_street text," +
            " address text,send_address text," +
            " tel text,send_tel text, mobile text,send_mobile text," +
            " telegram text,send_telegram text," +
            "totalTabaghe integer,send_totalTabaghe integer," +
            " totalVahed integer,send_totalVahed integer," +
            " vahed integer,send_vahed integer," +
            " tabaghe integer,send_tabaghe integer," +
            " hashieh integer,send_hashieh integer," +
            " rooms integer,send_rooms integer," +
            " descritpion text,send_descritpion text," +
            " tarakom integer,send_tarakom integer," +
            " masahat integer,send_metraj integer," +
            " latitude integer,longitude integer," +

            " zirBana integer,send_zirBana integer," +
            " arseZamin integer,send_arseZamin integer," +
            " omrSakhteman integer,send_omrSakhteman integer," +
//            " samayeshi integer,send_samayeshi integer," +
//            " garmayeshi integer,send_garmayeshi integer," +
//            " kaf integer,send_kaf integer, divar integer,send_divar integer," +
//            " nama integer,send_nama integer," +
//            " cabinet integer,send_cabinet integer," +
            " ab integer,send_ab integer," +
            " gaz integer,send_gaz integer," +
            " bargh integer,send_bargh integer," +
            " region integer,send_region integer," +
            " city integer,send_city integer, nodeid integer,send_nodeid integer," +
            "  status integer," +
            "  totalvisited integer,\n  day1cnt integer,\n  day2cnt integer,\n  day3cnt integer,\n  day4cnt integer\n  )";
    public static final String table_property_detail__table_create = "CREATE TABLE " + table_property_detail_name + " (\n" +
            "  id integer primary key AUTOINCREMENT,\n" +
            "  p_id integer,pd_id integer" +
            "  )";
    public static final String search_history_table_create = "CREATE TABLE search_history (\n" +
            "  id integer primary key AUTOINCREMENT,\n" +
            "  name text\n" +
            "  )";
    public static final String Property_Image_TABLE = "CREATE TABLE " + table_image_name +
            " ( p_id integer,\n" +
            "id integer primary key AUTOINCREMENT," +
            "localname text," +
            "remotename text," +
            "remote_id integer," +
            "main number DEFAULT 0," +
            "deleted number DEFAULT 0" +
            "  )";
//    public static final String Property_Payment_TABLE = "CREATE TABLE " + table_payment_name +
//            " ( Property_id integer,\n" +
//            "payDate text," +
//            "festivalDate text," +
//            "type number DEFAULT 0 )";

    private MySqliteOpenHelper(Context context) {
        super(context, "localdb", null, 1);
        loadProperty();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Property_TABLE);
        db.execSQL(Property_Image_TABLE);
//        db.execSQL(Property_Payment_TABLE);
        db.execSQL(search_history_table_create);
        db.execSQL(table_property_detail__table_create);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public String[] getHistorySearch() {
        String query = "select * from " + search_history_table_name;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String[] text = new String[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {
            text[i] = cursor.getString(cursor.getColumnIndex("name"));
            cursor.moveToNext();
        }
        return text;

    }

//    public void insertHistory(String text) {
//        ContentValues cv = new ContentValues();
//        cv.put("name", text); //These Fields should be your String values of actual column names
//        int num = getWritableDatabase().update(search_history_table_name, cv, "name='" + text + "'", null);
//        if (num == 0)
//            getWritableDatabase().insert(search_history_table_name, null, cv);
//    }

    public void loadProperty() {
        property=null;
        historyPropertys = new HashMap<>();
        myPropertys = new HashMap<>();
        myPropertysremote = new HashMap<>();
        allPropertys = new HashMap<>();
        bookmarkPropertys = new HashMap<>();
        String query = "select * from " + table_name;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Property[] property = new Property[cursor.getCount()];
        for (int i = 0; i < cursor.getCount(); i++) {
            property[i] = new Property();


            property[i].desc = cursor.getString(cursor.getColumnIndex("descritpion"));

            try {
                property[i].date = dateFormat.parse(cursor.getString(cursor.getColumnIndex("created")));
            } catch (Exception e) {
                e.printStackTrace();
            }
            property[i].local_id = cursor.getLong(cursor.getColumnIndex("local_id"));
            property[i].remote_id = cursor.getLong(cursor.getColumnIndex("remote_id"));
            property[i].title = cursor.getString(cursor.getColumnIndex("title"));
//            property[i].qr_code = cursor.getString(cursor.getColumnIndex("qr_code"));
            property[i].desc = cursor.getString(cursor.getColumnIndex("descritpion"));
            property[i].address = cursor.getString(cursor.getColumnIndex("address"));
            property[i].email = cursor.getString(cursor.getColumnIndex("email"));
            property[i].mobile = cursor.getString(cursor.getColumnIndex("mobile"));
            property[i].tel = cursor.getString(cursor.getColumnIndex("tel"));
            property[i].telegram = cursor.getString(cursor.getColumnIndex("telegram"));
            property[i].name = cursor.getString(cursor.getColumnIndex("name"));
            property[i].dateString = cursor.getString(cursor.getColumnIndex("created"));
            property[i].bookmark = cursor.getInt(cursor.getColumnIndex("bookmark"));
            property[i].status = cursor.getInt(cursor.getColumnIndex("status"));
            property[i].address = cursor.getString(cursor.getColumnIndex("address"));
            property[i].send_desc = cursor.getString(cursor.getColumnIndex("send_descritpion"));
            property[i].avenue = cursor.getString(cursor.getColumnIndex("avenue"));
            if (property[i].avenue == null)
                property[i].avenue = "";

            property[i].street = cursor.getString(cursor.getColumnIndex("street"));
            if (property[i].street == null)
                property[i].street = "";

            property[i].totalTabaghe = cursor.getInt(cursor.getColumnIndex("totalTabaghe"));
            property[i].totalVahed = cursor.getInt(cursor.getColumnIndex("totalVahed"));
            property[i].vahed = cursor.getInt(cursor.getColumnIndex("vahed"));
            property[i].tabaghe = cursor.getInt(cursor.getColumnIndex("tabaghe"));
            property[i].hashieh = cursor.getInt(cursor.getColumnIndex("hashieh"));
            property[i].rooms = cursor.getInt(cursor.getColumnIndex("rooms"));
            property[i].tarakom = cursor.getInt(cursor.getColumnIndex("tarakom"));
            property[i].masahat = cursor.getInt(cursor.getColumnIndex("masahat"));
            property[i].zirBana = cursor.getInt(cursor.getColumnIndex("zirBana"));
            property[i].arseZamin = cursor.getInt(cursor.getColumnIndex("arseZamin"));
            property[i].omrSakhteman = cursor.getInt(cursor.getColumnIndex("omrSakhteman"));
//            property[i].samayeshi = cursor.getInt(cursor.getColumnIndex("samayeshi"));
//            property[i].garmayeshi = cursor.getInt(cursor.getColumnIndex("garmayeshi"));
//            property[i].kaf = cursor.getInt(cursor.getColumnIndex("kaf"));
//            property[i].divar = cursor.getInt(cursor.getColumnIndex("divar"));
//            property[i].nama = cursor.getInt(cursor.getColumnIndex("nama"));
//            property[i].cabinet = cursor.getInt(cursor.getColumnIndex("cabinet"));
            property[i].ab = cursor.getInt(cursor.getColumnIndex("ab"));
            property[i].gaz = cursor.getInt(cursor.getColumnIndex("gaz"));
            property[i].bargh = cursor.getInt(cursor.getColumnIndex("bargh"));
            property[i].region = cursor.getInt(cursor.getColumnIndex("region"));
            property[i].city = cursor.getInt(cursor.getColumnIndex("city"));
            property[i].nodeid = cursor.getInt(cursor.getColumnIndex("nodeid"));

            property[i].send_totalTabaghe = cursor.getInt(cursor.getColumnIndex("send_totalTabaghe"));
            property[i].send_totalVahed = cursor.getInt(cursor.getColumnIndex("send_totalVahed"));
            property[i].send_vahed = cursor.getInt(cursor.getColumnIndex("send_vahed"));
            property[i].send_tabaghe = cursor.getInt(cursor.getColumnIndex("send_tabaghe"));
            property[i].send_hashieh = cursor.getInt(cursor.getColumnIndex("send_hashieh"));
            property[i].send_rooms = cursor.getInt(cursor.getColumnIndex("send_rooms"));
            property[i].send_tarakom = cursor.getInt(cursor.getColumnIndex("send_tarakom"));
            property[i].send_metraj = cursor.getInt(cursor.getColumnIndex("send_metraj"));
            property[i].send_zirBana = cursor.getInt(cursor.getColumnIndex("send_zirBana"));
            property[i].send_arseZamin = cursor.getInt(cursor.getColumnIndex("send_arseZamin"));
            property[i].send_omrSakhteman = cursor.getInt(cursor.getColumnIndex("send_omrSakhteman"));
//            property[i].send_samayeshi = cursor.getInt(cursor.getColumnIndex("send_samayeshi"));
//            property[i].send_garmayeshi = cursor.getInt(cursor.getColumnIndex("send_garmayeshi"));
//            property[i].send_kaf = cursor.getInt(cursor.getColumnIndex("send_kaf"));
//            property[i].send_divar = cursor.getInt(cursor.getColumnIndex("send_divar"));
//            property[i].send_nama = cursor.getInt(cursor.getColumnIndex("send_nama"));
//            property[i].send_cabinet = cursor.getInt(cursor.getColumnIndex("send_cabinet"));
            property[i].send_ab = cursor.getInt(cursor.getColumnIndex("send_ab"));
            property[i].send_gaz = cursor.getInt(cursor.getColumnIndex("send_gaz"));
            property[i].send_bargh = cursor.getInt(cursor.getColumnIndex("send_bargh"));
            property[i].send_region = cursor.getInt(cursor.getColumnIndex("send_region"));
            property[i].send_city = cursor.getInt(cursor.getColumnIndex("send_city"));
            property[i].send_nodeid = cursor.getInt(cursor.getColumnIndex("send_nodeid"));


            if (property[i].send_desc == null)
                property[i].send_desc = "";

            property[i].send_title = cursor.getString(cursor.getColumnIndex("send_title"));
            if (property[i].send_title == null)
                property[i].send_title = "";
            property[i].send_desc = cursor.getString(cursor.getColumnIndex("send_descritpion"));
            if (property[i].send_desc == null)
                property[i].send_desc = "";
            property[i].send_address = cursor.getString(cursor.getColumnIndex("send_address"));
            if (property[i].send_address == null)
                property[i].send_address = "";
            property[i].send_city = cursor.getInt(cursor.getColumnIndex("send_city"));
            property[i].send_region = cursor.getInt(cursor.getColumnIndex("send_region"));

            property[i].send_email = cursor.getString(cursor.getColumnIndex("send_email"));
            if (property[i].send_email == null)
                property[i].send_email = "";

            property[i].send_avenue = cursor.getString(cursor.getColumnIndex("send_avenue"));
            if (property[i].send_avenue == null)
                property[i].send_avenue = "";

            property[i].send_street = cursor.getString(cursor.getColumnIndex("send_street"));
            if (property[i].send_street == null)
                property[i].send_street = "";

            property[i].send_mobile = cursor.getString(cursor.getColumnIndex("send_mobile"));
            if (property[i].send_mobile == null)
                property[i].send_mobile = "";
            property[i].send_tel = cursor.getString(cursor.getColumnIndex("send_tel"));
            if (property[i].send_tel == null)
                property[i].send_tel = "";
            property[i].send_telegram = cursor.getString(cursor.getColumnIndex("send_telegram"));
            if (property[i].send_telegram == null)
                property[i].send_telegram = "";
            property[i].send_name = cursor.getString(cursor.getColumnIndex("send_name"));
            if (property[i].send_name == null)
                property[i].send_name = "";
            property[i].send_nodeid = cursor.getInt(cursor.getColumnIndex("send_nodeid"));

            property[i].send_address = cursor.getString(cursor.getColumnIndex("send_address"));
            if (property[i].send_address == null)
                property[i].send_address = "";


            property[i].totalVisited = cursor.getLong(cursor.getColumnIndex("totalvisited"));
            property[i].day1Cnt = cursor.getLong(cursor.getColumnIndex("day1cnt"));
            property[i].day2Cnt = cursor.getLong(cursor.getColumnIndex("day2cnt"));
            property[i].day3Cnt = cursor.getLong(cursor.getColumnIndex("day3cnt"));
            property[i].day4Cnt = cursor.getLong(cursor.getColumnIndex("day4cnt"));

            property[i].myproperty = cursor.getInt(cursor.getColumnIndex("myproperty"));
            allPropertys.put(property[i].local_id, property[i]);
            if (property[i].myproperty == 1) {
                property[i].setLoaded(true);
                if (property[i].status == DRAFT_STATUS) {
                    this.property = property[i];

                }
                myPropertys.put(property[i].local_id, property[i]);
                if (property[i].remote_id != 0)
                    myPropertysremote.put(property[i].remote_id, property[i]);
            } else if (property[i].bookmark == 1)
                bookmarkPropertys.put(property[i].remote_id, property[i]);
            else
                historyPropertys.put(property[i].remote_id, property[i]);


            cursor.moveToNext();
        }
        cursor.close();
//        db.close();

        query = "select * from " + table_image_name;
//        db = getReadableDatabase();
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {

            long local_id = cursor.getLong(cursor.getColumnIndex("p_id"));
            Property userProperty = allPropertys.get(local_id);
            if (userProperty == null) {
                cursor.moveToNext();
                continue;
            }
            Property.Image image=new Property.Image();
            image.localname = cursor.getString(cursor.getColumnIndex("localname"));
            image.remotename = cursor.getString(cursor.getColumnIndex("remotename"));
            image.remote_Id = cursor.getInt(cursor.getColumnIndex("remote_id"));
            image.main = cursor.getInt(cursor.getColumnIndex("main"));
            image.deleted = cursor.getInt(cursor.getColumnIndex("deleted"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            userProperty.images.add(image);
//            userProperty.addImage(id, (localname == null) ? localname : FileUtils.getInstance().getImageFile(localname).getAbsolutePath(), (remotename == null || remotename.equals("-1")) ? null : remotename, main, deleted);


            cursor.moveToNext();
        }
        query = "select * from " + table_property_detail_name;
//        db = getReadableDatabase();
        cursor.close();
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {

            long local_id = cursor.getLong(cursor.getColumnIndex("p_id"));
            Property userProperty = allPropertys.get(local_id);
            if (userProperty == null) {
                cursor.moveToNext();
                continue;
            }
            int detail_id = cursor.getInt(cursor.getColumnIndex("pd_id"));

            userProperty.details.add(DBAdapter.getInstance().allPropertyDetail.get(detail_id));


            cursor.moveToNext();
        }
        cursor.close();


        db.close();


    }

    public long insertORUpdateProperty(Property property) {
        ContentValues contentValues = new ContentValues();
        if (property.mobile != null) contentValues.put("mobile", property.mobile);
        if (property.remote_id != 0) contentValues.put("remote_id", property.remote_id);
        if (property.tel != null) contentValues.put("tel", property.tel);
        if (property.address != null) contentValues.put("address", property.tel);
        if (property.street != null) contentValues.put("street", property.street);
        if (property.avenue != null) contentValues.put("avenue", property.avenue);
        if (property.email != null) contentValues.put("email", property.email);
        if (property.name != null) contentValues.put("name", property.name);
        if (property.address != null) contentValues.put("address", property.address);
        if (property.dateString != null) contentValues.put("created", property.dateString);
        if (property.title != null) contentValues.put("title", property.title);
        if (property.desc != null) contentValues.put("descritpion", property.desc);
        if (property.telegram != null) contentValues.put("telegram", property.telegram);
        if (property.date != null) contentValues.put("created", dateFormat.format(property.date));
        contentValues.put("totalTabaghe", property.totalTabaghe);
        contentValues.put("totalVahed", property.totalVahed);
        contentValues.put("vahed", property.vahed);
        contentValues.put("tabaghe", property.tabaghe);
        contentValues.put("hashieh", property.hashieh);
        contentValues.put("rooms", property.rooms);
        contentValues.put("tarakom", property.tarakom);
        contentValues.put("masahat", property.masahat);
        contentValues.put("zirBana", property.zirBana);
        contentValues.put("arseZamin", property.arseZamin);
        contentValues.put("omrSakhteman", property.omrSakhteman);
//        contentValues.put("samayeshi", property.samayeshi);
//        contentValues.put("garmayeshi", property.garmayeshi);
//        contentValues.put("kaf", property.kaf);
//        contentValues.put("divar", property.divar);
//        contentValues.put("nama", property.nama);
//        contentValues.put("cabinet", property.cabinet);
        contentValues.put("ab", property.ab);
        contentValues.put("gaz", property.gaz);
        contentValues.put("bargh", property.bargh);
        contentValues.put("region", property.region);
        contentValues.put("city", property.city);
        contentValues.put("nodeid", property.nodeid);
        if (property.send_mobile != null) contentValues.put("send_mobile", property.send_mobile);

        if (property.send_tel != null) contentValues.put("send_tel", property.send_tel);
        if (property.send_address != null) contentValues.put("send_address", property.send_tel);
        if (property.send_street != null) contentValues.put("send_street", property.send_street);
        if (property.send_avenue != null) contentValues.put("send_avenue", property.send_avenue);
        if (property.send_email != null) contentValues.put("send_email", property.send_email);
        if (property.send_name != null) contentValues.put("send_name", property.send_name);
        if (property.send_address != null) contentValues.put("send_address", property.send_address);
        if (property.send_title != null) contentValues.put("send_title", property.send_title);
        if (property.send_desc != null) contentValues.put("send_descritpion", property.send_desc);
        if (property.send_telegram != null)
            contentValues.put("send_telegram", property.send_telegram);
        contentValues.put("send_totalTabaghe", property.send_totalTabaghe);
        contentValues.put("send_totalVahed", property.send_totalVahed);
        contentValues.put("send_vahed", property.send_vahed);
        contentValues.put("send_tabaghe", property.send_tabaghe);
        contentValues.put("send_hashieh", property.send_hashieh);
        contentValues.put("send_rooms", property.send_rooms);
        contentValues.put("send_tarakom", property.send_tarakom);
        contentValues.put("send_metraj", property.send_metraj);
        contentValues.put("send_zirBana", property.send_zirBana);
        contentValues.put("send_arseZamin", property.send_arseZamin);
        contentValues.put("send_omrSakhteman", property.send_omrSakhteman);
//        contentValues.put("send_samayeshi", property.send_samayeshi);
//        contentValues.put("send_garmayeshi", property.send_garmayeshi);
//        contentValues.put("send_kaf", property.send_kaf);
//        contentValues.put("send_divar", property.send_divar);
//        contentValues.put("send_nama", property.send_nama);
//        contentValues.put("send_cabinet", property.send_cabinet);
        contentValues.put("send_ab", property.send_ab);
        contentValues.put("send_gaz", property.send_gaz);
        contentValues.put("send_bargh", property.send_bargh);
        contentValues.put("send_region", property.send_region);
        contentValues.put("send_city", property.send_city);
        contentValues.put("send_nodeid", property.send_nodeid);


        if (property.totalVisited != 0) contentValues.put("totalvisited", property.totalVisited);
        if (property.day1Cnt != 0) contentValues.put("day1Cnt", property.day1Cnt);
        if (property.day2Cnt != 0) contentValues.put("day2Cnt", property.day2Cnt);
        if (property.day3Cnt != 0) contentValues.put("day3Cnt", property.day3Cnt);
        if (property.day4Cnt != 0) contentValues.put("day4Cnt", property.day4Cnt);
        contentValues.put("bookmark", property.bookmark);
        contentValues.put("status", property.status);

        contentValues.put("myproperty", property.myproperty);


        SQLiteDatabase writableDatabase = getWritableDatabase();
        if (property.local_id == 0) {
            long insert = writableDatabase.insert(table_name, null, contentValues);
            property.local_id = insert;

        } else {
            writableDatabase.update(table_name, contentValues, "local_id=" + property.local_id, null);

        }
        writableDatabase.close();
        insertORUpdatePropertyDetail(property);

        insertORUpdatePropertyImage(property);


        writableDatabase.close();
        loadProperty();
        return property.local_id;
//        property.sync();
    }

    private void insertORUpdatePropertyDetail(Property property) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        int count = writableDatabase.delete(table_property_detail_name, "p_id=" + property.local_id, null);
        for (int i = 0; i < property.details.size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("p_id", property.local_id);
            contentValues.put("pd_id", property.details.get(i).id);
            writableDatabase.insert(table_property_detail_name, null, contentValues);
        }
        writableDatabase.close();

    }


//    private void insertOrUpdateUserPayment(Property userProperty) {
//        if (userProperty.images.size() == 0)
//            return;
//        SQLiteDatabase writableDatabase = getWritableDatabase();
//        writableDatabase.delete(table_payment_name, "Property_id=" + userProperty.local_id, null);
//        ContentValues contentValues = new ContentValues();
//        for (int i = 0; i < userProperty.payments.size(); i++) {
//            Property.Payment image = userProperty.payments.get(i);
//            if (image.payDate != null)
//                contentValues.put("payDate", dateFormat.format(image.payDate));
//            if (image.festivalDate != null)
//                contentValues.put("festivalDate", dateFormat2.format(image.festivalDate));
//            contentValues.put("type", image.type);
//            contentValues.put("Property_id", userProperty.local_id);
//            writableDatabase.insert(table_payment_name, null, contentValues);
//
//        }
//        writableDatabase.close();
//
//
//    }

    private void insertORUpdatePropertyImage(Property property) {
        if (true) {
            SQLiteDatabase writableDatabase = getWritableDatabase();
            int count=writableDatabase.delete(table_image_name, "p_id=" + property.local_id, null);
            for (int i = 0; i < property.images.size(); i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("p_id", property.local_id);
                contentValues.put("remote_id", property.images.get(i).remote_Id);
                contentValues.put("remotename", property.images.get(i).remotename);
                contentValues.put("localname", property.images.get(i).localname);
                contentValues.put("main", property.images.get(i).main);
                contentValues.put("deleted", property.images.get(i).deleted);
                long insert = writableDatabase.insert(table_image_name, null, contentValues);
                System.out.println("------------------------          "+insert);
            }
            writableDatabase.close();
        } else {

            SQLiteDatabase writableDatabase = getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            boolean hasmain = false;
//            for (int i = 0; i < property.images.size(); i++) {
//                hasmain = property.images.get(i).main && !property.images.get(i).deleted;
//            }
//            if (!hasmain)
//                property.images.get(0).main = true;


            boolean a;
            for (int i = 0; i < property.images.size(); i++) {
                a = false;
                Property.Image image = property.images.get(i);
//                if (image.main) {
//                    contentValues.put("main", 1);
//                } else {
//                    contentValues.put("main", 0);
//                }
                if (image.remotename != null && !image.remotename.equals("null")) {
                    contentValues.put("remote_name", Utils.getName(image.remotename));
                    a = true;
                }
//                if (image.localname != null && !image.localname.equals("null")) {
//                    if (property.myproperty == 1 && !FileUtils.getInstance().existInDefaultFoder(image.localname) && !image.deleted) {
//                        image.localname = FileUtils.getInstance().copyToDefaultFoder(image.localname);
//                    }
//                    contentValues.put("local_name", Utils.getName(image.localname));
//                    a = true;
//                }
                if (a) {
                    contentValues.put("local_id", property.local_id);
                    contentValues.put("deleted", image.deleted);
                    if (image.id == 0) {
                        long insert = writableDatabase.insert(table_image_name, null, contentValues);
                        image.id = insert;
                    } else {
                        writableDatabase.update(table_image_name, contentValues, "id=" + image.id, null);
                    }

                }
            }


            writableDatabase.close();
        }


    }


    public void delete(Property userProperty) {
        delete(userProperty.local_id);
        historyPropertys.remove(userProperty.remote_id);
        allPropertys.remove(userProperty.local_id);
        myPropertys.remove(userProperty.local_id);

//        if (userProperty.remote_id != 0)
//            remoteUserProperty.remove(userProperty.remote_id);


    }

    public void delete(long id) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete(table_name, "local_id=" + id, null);
        writableDatabase.close();

    }

    public void deleteHistory() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete(table_name, "(myProperty is null or myProperty<>1) and (bookmark is null or bookmark<>1)", null);
        writableDatabase.close();
        loadProperty();
    }

    public void deleteBookmark() {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        writableDatabase.delete(table_name, " bookmark =1", null);
        writableDatabase.close();
        loadProperty();
    }
}
