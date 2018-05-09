package com.dataservicios.ttauditalicorppreventagalleta.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.dataservicios.ttauditalicorppreventagalleta.model.Audit;
import com.dataservicios.ttauditalicorppreventagalleta.model.AuditRoadStore;
import com.dataservicios.ttauditalicorppreventagalleta.model.Company;
import com.dataservicios.ttauditalicorppreventagalleta.model.Distributor;
import com.dataservicios.ttauditalicorppreventagalleta.model.Media;
import com.dataservicios.ttauditalicorppreventagalleta.model.Poll;
import com.dataservicios.ttauditalicorppreventagalleta.model.PollDetail;
import com.dataservicios.ttauditalicorppreventagalleta.model.PollOption;
import com.dataservicios.ttauditalicorppreventagalleta.model.Product;
import com.dataservicios.ttauditalicorppreventagalleta.model.Route;
import com.dataservicios.ttauditalicorppreventagalleta.model.RouteStoreTime;
import com.dataservicios.ttauditalicorppreventagalleta.model.Store;
import com.dataservicios.ttauditalicorppreventagalleta.model.User;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jaime on 28/08/2016.
 */
public class AuditUtil {
    public static final String LOG_TAG = AuditUtil.class.getSimpleName();
    //private AlbumStorageDirFactory mAlbumStorageDirFactory = null;
    private Context context;

    public AuditUtil(Context context) {
        this.context = context ;
    }

    public boolean uploadMedia(Media media, int typeSend){
        HttpURLConnection httpConnection = null;
        final String url_upload_image = GlobalConstant.dominio + "/insertImagesMayorista";
        File file = new File(BitmapLoader.getAlbumDirTemp(context).getAbsolutePath() + "/" + media.getFile());
        if(!file.exists()){
            return true;
        }
        Bitmap bbicon = null;
        //Bitmap scaledBitmap;
        bbicon = BitmapLoader.loadBitmap(file.getAbsolutePath(),280,280);

//        if(Build.MODEL.equals("MotoG3")){
//            //scaledBitmap = BitmapLoader.scaleDown() BitmapLoader.rotateImage(bbicon,0);
//            scaledBitmap = BitmapLoader.rotateImage(BitmapLoader.scaleDown(bbicon, 540 , true),0);
//        } else {
//            //scaledBitmap = BitmapLoader.rotateImage(bbicon,90);
//            scaledBitmap = BitmapLoader.rotateImage(BitmapLoader.scaleDown(bbicon, 540 , true),90);
//        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bbicon.compress(Bitmap.CompressFormat.JPEG,50, bos);
        try {

            ContentBody photo = new ByteArrayBody(bos.toByteArray(), file.getName());
            AndroidMultiPartEntity mpEntity = new AndroidMultiPartEntity(new AndroidMultiPartEntity.ProgressListener() {
                @Override
                public void transferred(long num) {
                    //notification.contentView.setProgressBar(R.id.progressBar1, 100,(int) ((num / (float) totalSize) * 100), true);
                    // notificationManager.notify(1, notification);
                }
            });

            mpEntity.addPart("fotoUp"               , photo                                                             );
            mpEntity.addPart("archivo"              , new StringBody(String.valueOf(file.getName()))                    );
            mpEntity.addPart("store_id"             , new StringBody(String.valueOf(media.getStore_id()))               );
            mpEntity.addPart("product_id"           , new StringBody(String.valueOf(media.getProduct_id()))             );
            mpEntity.addPart("poll_id"              , new StringBody(String.valueOf(media.getPoll_id()))                );
            mpEntity.addPart("publicities_id"       , new StringBody(String.valueOf(media.getPublicity_id()))           );
            mpEntity.addPart("category_product_id"  , new StringBody(String.valueOf(media.getCategory_product_id()))    );
            mpEntity.addPart("company_id"           , new StringBody(String.valueOf(media.getCompany_id()))             );
            mpEntity.addPart("tipo"                 , new StringBody(String.valueOf(media.getType()))                   );
            mpEntity.addPart("monto"                , new StringBody(String.valueOf(media.getMonto()))                  );
            mpEntity.addPart("razon_social"         , new StringBody(String.valueOf(media.getRazonSocial()))            );
            mpEntity.addPart("horaSistema"          , new StringBody(String.valueOf(media.getCreated_at()))             );

            URL url = new URL(url_upload_image);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setReadTimeout(10000);
            httpConnection.setConnectTimeout(15000);
            httpConnection.setRequestMethod("POST");
            httpConnection.setUseCaches(false);
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setRequestProperty("Connection", "Keep-Alive");
            httpConnection.addRequestProperty("Content-length", mpEntity.getContentLength()+"");
            httpConnection.addRequestProperty(mpEntity.getContentType().getName(), mpEntity.getContentType().getValue());
            OutputStream os = httpConnection.getOutputStream();
            mpEntity.writeTo(httpConnection.getOutputStream());
            os.close();
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Log.d("UPLOAD", "HTTP 200 OK." + httpConnection.getResponseCode()+" "+httpConnection.getResponseMessage()+".");
                //return readStream(httpConnection.getInputStream());
                //This return returns the response from the upload.

                if(file.exists()){
                    file.delete();
                }
                return  true ;

            } else {
                Log.d("UPLOAD", "HTTP "+httpConnection.getResponseCode()+" "+httpConnection.getResponseMessage()+".");
                return  false ;
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return  false ;
        }finally {
            if(httpConnection != null){
                httpConnection.disconnect();
            }
        }
        //return true;
    }

    /**
     * Cierra una Auditoría determinada, de una tienda determinada en su ruta
     * @param audit_id
     * @param store_id
     * @param company_id
     * @param route_id
     * @return true si se realizó con éxito
     */
    public static boolean closeAuditStore(int audit_id, int store_id, int company_id,int route_id) {
        int success;
        try {
            HashMap<String, String> params = new HashMap<>();

            params.put("audit_id"       , String.valueOf(audit_id)  );
            params.put("store_id"       , String.valueOf(store_id)  );
            params.put("company_id"     , String.valueOf(company_id));
            params.put("rout_id"        , String.valueOf(route_id)  );

            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/closeAudit" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());

            // json success, tag que retorna el json

            if (json == null) {
                Log.d("JSON result", "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente");
                    return true;
                }else{
                    Log.d(LOG_TAG, "no insertó registro");
                    // return json.getString("message");
                    // return false;
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Obtien lista de productos segun un typo
     * @param company_id
     * @param type Tipo = 1 : competencias
     * @return
     */
    public static ArrayList<Product> getListProductsCompetity(int company_id, int type){
        int success ;

        ArrayList<Product> productsCompetity = new ArrayList<Product>();
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("ajax", String.valueOf("1"));
            params.put("company_id", String.valueOf(company_id));
            params.put("competition", String.valueOf(type));
            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            //JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonRoadsDetail" ,"POST", params);
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/ajaxGetProductsCompetition" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");

            } else{
                success = json.getInt("success");
                if (success > 0) {
                    JSONArray ObjJson;
                    ObjJson = json.getJSONArray("products");
                    // looping through All Products
                    if(ObjJson.length() > 0) {

                        for (int i = 0; i < ObjJson.length(); i++) {
                            JSONObject obj = ObjJson.getJSONObject(i);
                            Product product = new Product();

                            product.setId(obj.getInt("id"));
                            product.setCompany_id(obj.getInt("company_id"));
                            product.setCategory_product_id(1);
                            if(obj.isNull("fullname")) product.setFullname("");  else product.setFullname(obj.getString("fullname"));
                            if(obj.isNull("precio")) product.setPrecio("");  else product.setPrecio(obj.getString("precio"));
                            if(obj.isNull("imagen")) product.setImagen("/media/images/");  else product.setImagen(GlobalConstant.dominio + "/media/images/" + obj.getString("imagen"));;
                            if(obj.isNull("composicion")) product.setComposicion("");  else product.setComposicion(obj.getString("composicion"));
                            if(obj.isNull("fabricante")) product.setFabricante("");  else product.setFabricante(obj.getString("fabricante"));
                            if(obj.isNull("presentacion")) product.setPresentacion("");  else product.setPresentacion(obj.getString("presentacion"));
                            if(obj.isNull("unidad")) product.setUnidad("");  else product.setUnidad(obj.getString("unidad"));
                            product.setType(1);
                            product.setCreated_at(obj.getString("created_at"));
                            product.setUpdated_at(obj.getString("updated_at"));
                            productsCompetity.add(i, product);
                        }

                    }
                    Log.d(LOG_TAG, "Ingresado correctamente");
                }else{
                    Log.d(LOG_TAG, "No se ingreso el registro");
                    //return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // return false;
        }
        return productsCompetity;
    }


    /**
     * Cierra la ruta en inserta el tiempo de la auditoría
     * @param routeStoreTIme
     * @return true si se realizó con éxito
     */
    public static boolean closeRouteStore(RouteStoreTime routeStoreTIme) {


        int success;
        try {
            HashMap<String, String> paramsData = new HashMap<>();
            paramsData.put("latitud_close"  , String.valueOf(routeStoreTIme.getLat_close()) );
            paramsData.put("longitud_close" , String.valueOf(routeStoreTIme.getLon_close()) );
            paramsData.put("latitud_open"   , String.valueOf(routeStoreTIme.getLat_open())  );
            paramsData.put("longitud_open"  , String.valueOf(routeStoreTIme.getLon_open())  );
            paramsData.put("tiempo_inicio"  , routeStoreTIme.getTime_open()                 );
            paramsData.put("tiempo_fin"     , routeStoreTIme.getTime_close()                );
            paramsData.put("tduser"         , String.valueOf(routeStoreTIme.getUser_id())   );
            paramsData.put("id"             , String.valueOf(routeStoreTIme.getStore_id())  );
            paramsData.put("idruta"         , String.valueOf(routeStoreTIme.getRoute_id())  );
            paramsData.put("company_id"     , String.valueOf(routeStoreTIme.getCompany_id()));

            Log.d("request", "starting");
            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            //JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/json/prueba.json", "POST", paramsData);
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/insertaTiempoNew", "POST", paramsData);
            if (json == null) {
                Log.d("JSON result", "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente");
                    return true;
                }else{
                    Log.d(LOG_TAG, "no insertó registro");
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    /**
     * Iserta la respuest de una pregunta a la tabla PollDetail
     * @param pollDetail
     * @return true si se realizó con éxito
     */
    public static boolean insertPollDetail(PollDetail pollDetail) {
        int success;
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("poll_id"                , String.valueOf(pollDetail.getPoll_id()));
            params.put("store_id"               , String.valueOf(pollDetail.getStore_id()));
            params.put("sino"                   , String.valueOf(pollDetail.getSino()));
            params.put("options"                , String.valueOf(pollDetail.getOptions()));
            params.put("limits"                 , String.valueOf(pollDetail.getLimits()));
            params.put("media"                  , String.valueOf(pollDetail.getMedia()));
            params.put("coment"                 , String.valueOf(pollDetail.getComment()));
            params.put("result"                 , String.valueOf(pollDetail.getResult()));
            params.put("limite"                 , String.valueOf(pollDetail.getLimite()));
            params.put("comentario"             , String.valueOf(pollDetail.getComentario()));
            params.put("auditor"                , String.valueOf(pollDetail.getAuditor()));
            params.put("product_id"             , String.valueOf(pollDetail.getProduct_id()));
            params.put("publicity_id"           , String.valueOf(pollDetail.getPublicity_id()));
            params.put("company_id"             , String.valueOf(pollDetail.getCompany_id()));
            params.put("category_product_id"    , String.valueOf(pollDetail.getCategory_product_id()));
            params.put("commentOptions"         , String.valueOf(pollDetail.getCommentOptions()));
            params.put("selectedOptions"        , String.valueOf(pollDetail.getSelectdOptions()));
            params.put("selectedOptionsComment" , String.valueOf(pollDetail.getSelectedOtionsComment()));
            params.put("priority"               , String.valueOf(pollDetail.getPriority()));

            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            //JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/json/prueba.json" ,"POST", params);
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/savePollDetailsReg" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente");
                    return true ;
                }else{
                    Log.d(LOG_TAG, "no insertó registro");
                    // return json.getString("message");
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cierra todas las auditorias del Store
     * @param store_id
     * @param company_id
     * @return
     */
    public static boolean closeAllAuditRoadStore(int store_id, int company_id) {
        int success;
        try {

            HashMap<String, String> params = new HashMap<>();


            params.put("store_id"               , String.valueOf(store_id));
            params.put("company_id"             , String.valueOf(company_id));


            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            //JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/json/prueba.json" ,"POST", params);
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/admin_api/api_close_audit_road_stores.php" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success > 0) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente");
                    return true ;
                }else{
                    Log.d(LOG_TAG, "no insertó registro");
                    // return json.getString("message");
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validate user and password for login
     * @param userName Nombre de Usuario
     * @param password Contraseña del usuario
     * @param imei Imei del dispositivo
     * @return User
     */
    public static User userLogin(String userName, String password , String imei){

        int success ;
        User user = new User();
        try {
            HashMap<String, String> params = new HashMap<>();

            params.put("username", String.valueOf(userName));
            params.put("password", String.valueOf(password));
            params.put("imei", String.valueOf(imei));
            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/loginMovil" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nulo");
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    user.setId(json.getInt("id"));
                    user.setEmail(userName);
                    user.setFullname(json.getString("fullname"));
                    if(json.isNull("image")) user.setImage("null");  else user.setImage(json.getString("image"));
                    user.setPassword(password);
                }else{
                    Log.d(LOG_TAG, "No se pudo iniciar sesión");
                    //return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // return false;
        }
        return  user;
    }




    /**
     * Obtiene una lista de Rutas
     * @param user_id
     * @param company_id
     * @return
     */
    public static ArrayList<Route> getListRoutes(int user_id, int company_id){
        int success ;

        ArrayList<Route> list = new ArrayList<Route>();
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("id", String.valueOf(user_id));
            params.put("company_id", String.valueOf(company_id));

            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonRoadsTotal" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");

            } else{
                success = json.getInt("success");
                if (success == 1) {
                    JSONArray ObjJson;
                    ObjJson = json.getJSONArray("roads");
                    // looping through All Products
                    if(ObjJson.length() > 0) {
                        for (int i = 0; i < ObjJson.length(); i++) {
                            JSONObject obj = ObjJson.getJSONObject(i);
                            Route route = new Route();
                            route.setId(Integer.valueOf(obj.getString("id")));
                            route.setFullname(String.valueOf(obj.getString("fullname")));
                            route.setAudit(Integer.valueOf(obj.getString("auditados")));
                            route.setTotal_store(Integer.valueOf(obj.getString("pdvs")));
                            list.add(i,route);
                        }
                    }
                    Log.d(LOG_TAG, "Ingresado correctamente");
                }else{
                    Log.d(LOG_TAG, "No se ingreso el registro");
                    //return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // return false;
        }
        return  list;
    }

    /**
     * Obtiene una lista de Stores
     * @param user_id
     * @param company_id
     * @return
     */
    public static ArrayList<Store> getListStores(int user_id, int company_id){
        int success ;

        ArrayList<Store> stores= new ArrayList<Store>();
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("user_id", String.valueOf(user_id));
            params.put("company_id", String.valueOf(company_id));

            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            //JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonRoadsDetail" ,"POST", params);
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/admin_api/api_JsonRoadsDetail.php" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");

            } else{
                success = json.getInt("success");
                if (success > 0) {
                    JSONArray ObjJson;
                    ObjJson = json.getJSONArray("roadsDetail");
                    // looping through All Products
                    if(ObjJson.length() > 0) {

                        for (int i = 0; i < ObjJson.length(); i++) {
                            JSONObject obj = ObjJson.getJSONObject(i);
                            Store store = new Store();
                            store.setId(obj.getInt("id"));
                            store.setRoute_id(obj.getInt("road_id"));
                            if(obj.isNull("fullname")) store.setFullname("");  else store.setFullname(obj.getString("fullname"));;
                            if(obj.isNull("cadenaRuc")) store.setCadenRuc("");  else store.setCadenRuc(obj.getString("cadenaRuc"));;
                            if(obj.isNull("documento")) store.setDocument("");  else store.setDocument(obj.getString("documento"));;
                            if(obj.isNull("tipo_documento")) store.setTypo_document("");  else store.setTypo_document(obj.getString("tipo_documento"));;
                            if(obj.isNull("region")) store.setRegion("");  else store.setRegion(obj.getString("region"));;
                            if(obj.isNull("tipo_bodega")) store.setTypeBodega("");  else store.setTypeBodega(obj.getString("tipo_bodega"));
                            if(obj.isNull("address")) store.setAddress("");  else store.setAddress(obj.getString("address"));
                            if(obj.isNull("district")) store.setDistrict("");  else store.setDistrict(obj.getString("district"));
                            store.setStatus(obj.getInt("status"));
                            if(obj.isNull("codclient")) store.setCodCliente("");  else store.setCodCliente(obj.getString("codclient"));
                            if(obj.isNull("urbanization")) store.setUrbanization("");  else store.setUrbanization(obj.getString("urbanization"));
                            if(obj.isNull("type")) store.setType("");  else store.setType(obj.getString("type"));
                            if(obj.isNull("ejecutivo")) store.setEjecutivo("");  else store.setEjecutivo(obj.getString("ejecutivo"));
                            if(obj.isNull("latitude")) store.setLatitude(0.0);  else store.setLatitude(obj.getDouble("latitude"));
                            if(obj.isNull("longitude")) store.setLongitude(0.0);  else store.setLongitude(obj.getDouble("longitude"));
                            if(obj.isNull("telephone")) store.setTelephone("");  else store.setTelephone(obj.getString("telephone"));
                            if(obj.isNull("cell")) store.setCell("");  else store.setCell(obj.getString("cell"));
                            if(obj.isNull("comment")) store.setComment("");  else store.setComment(obj.getString("comment"));
                            if(obj.isNull("owner")) store.setOwner("");  else store.setOwner(obj.getString("owner"));
                            if(obj.isNull("fnac")) store.setFnac("");  else store.setFnac(obj.getString("fnac"));


                            stores.add(i,store);
                        }

                    }
                    Log.d(LOG_TAG, "Ingresado correctamente");
                }else{
                    Log.d(LOG_TAG, "No se ingreso el registro");
                    //return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // return false;
        }
        return  stores;
    }

    /**
     * Obtiene el company_id campaña actual
     * @param active
     * @param visible
     * @param app_id
     * @return
     */
    public static Company getCompany(int active,int visible,String app_id){
        int success ;

        Company company = new Company();
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("app_id", String.valueOf(app_id));
            params.put("active", String.valueOf(active));
            params.put("visible", String.valueOf(visible));

            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/admin_api/api_company_app.php" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");

            } else{


                success = json.getInt("success");
                if (success == 1) {
                    JSONArray ObjJson;
                    ObjJson = json.getJSONArray("company");
                    // looping through All Products
                    if(ObjJson.length() > 0) {
                        for (int i = 0; i < ObjJson.length(); i++) {
                            JSONObject obj = ObjJson.getJSONObject(i);

                            company.setId(obj.getInt("id"));
                            company.setFullname(obj.getString("fullname"));
                            company.setActive(obj.getInt("active"));
                            company.setCustomer_id(obj.getInt("customer_id"));
                            company.setVisible(obj.getInt("visible"));
                            company.setAuditory(obj.getInt("auditoria"));
                            company.setLogo(obj.getString("logo"));
                            company.setMarkerPoint(obj.getString("markerPoint"));
                            company.setApp_id(obj.getString("app_id"));
                            company.setCreated_at(obj.getString("created_at"));
                            company.setUpdated_at(obj.getString("updated_at"));
                        }
                    }
                    Log.d(LOG_TAG, "Ingresado correctamente");
                }else{
                    Log.d(LOG_TAG, "No se ingreso el registro");
                    //return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // return false;
        }
        return  company;
    }

    /**
     * Obtiene todas las auditorías de un company_id determinado
     * @param company_id
     * @return
     */
    public static ArrayList<Audit> getAudits(int company_id){
        int success ;

        ArrayList<Audit> audits= new ArrayList<Audit>();
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("company_id", String.valueOf(company_id));

            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/admin_api/api_company_audits.php" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");

            } else{
                success = json.getInt("success");
                if (success > 0) {
                    JSONArray ObjJson;
                    ObjJson = json.getJSONArray("audits");
                    // looping through All Products
                    if(ObjJson.length() > 0) {

                        for (int i = 0; i < ObjJson.length(); i++) {
                            JSONObject obj = ObjJson.getJSONObject(i);
                            Audit audit = new Audit();
                            audit.setId(obj.getInt("id"));
                            audit.setCompany_audit_id(obj.getInt("company_audit_id"));
                            audit.setCompany_id(obj.getInt("company_id"));
                            if(obj.isNull("fullname")) audit.setFullname("");  else audit.setFullname(obj.getString("fullname"));;
                            audit.setOrden(obj.getInt("orden"));
                            audit.setAudit(obj.getInt("audit"));
                            if(obj.isNull("created_at")) audit.setCreated_at("");  else audit.setCreated_at(obj.getString("created_at"));;
                            if(obj.isNull("updated_at")) audit.setUpdated_at("");  else audit.setUpdated_at(obj.getString("updated_at"));;
                            audits.add(i,audit);
                        }
                    }
                    Log.d(LOG_TAG, "Ingresado correctamente");
                }else{
                    Log.d(LOG_TAG, "No se ingreso el registro");
                    //return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // return false;
        }
        return  audits;
    }

    /**
     * Obtiene una lista de todas las auditorias y las rutas de los stores
     * @param company_id
     * @param user_id
     * @return
     */
    public static ArrayList<AuditRoadStore> getAuditRoadStores(int company_id, int user_id){
        int success ;

        ArrayList<AuditRoadStore> auditRoadsStores= new ArrayList<AuditRoadStore>();
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("company_id", String.valueOf(company_id));
            params.put("user_id", String.valueOf(user_id));
            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/admin_api/api_audit_road_stores.php" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");

            } else{
                success = json.getInt("success");
                if (success > 0) {
                    JSONArray ObjJson;
                    ObjJson = json.getJSONArray("audits");
                    // looping through All Products
                    if(ObjJson.length() > 0) {

                        for (int i = 0; i < ObjJson.length(); i++) {
                            JSONObject obj = ObjJson.getJSONObject(i);
                            AuditRoadStore auditRoadStore = new AuditRoadStore();
                            auditRoadStore.setId(obj.getInt("id"));
                            auditRoadStore.setRoad_id(obj.getInt("road_id"));
                            auditRoadStore.setAudit_id(obj.getInt("audit_id"));
                            auditRoadStore.setStore_id(obj.getInt("store_id"));
                            auditRoadStore.setAuditStatus(obj.getInt("audit"));

                            auditRoadsStores.add(i,auditRoadStore);
                        }
                    }
                    Log.d(LOG_TAG, "Ingresado correctamente");
                }else{
                    Log.d(LOG_TAG, "No se ingreso el registro");
                    //return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // return false;
        }
        return  auditRoadsStores;
    }

    /**
     * Obtiene una unica Ruta con las auditorias y  stores
     * @param company_id
     * @param road_id
     * @param store_id
     * @return Retorna un Lista de  AuditRoadStore
     */
    public static ArrayList<AuditRoadStore> getAuditRoadStore(int company_id, int road_id, int store_id){
        int success ;

        ArrayList<AuditRoadStore> auditRoadsStores= new ArrayList<AuditRoadStore>();
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("company_id", String.valueOf(company_id));
            params.put("road_id", String.valueOf(road_id));
            params.put("store_id", String.valueOf(store_id));
            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/admin_api/api_audit_road_store.php" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");

            } else{
                success = json.getInt("success");
                if (success > 0) {
                    JSONArray ObjJson;
                    ObjJson = json.getJSONArray("audits");
                    // looping through All Products
                    if(ObjJson.length() > 0) {

                        for (int i = 0; i < ObjJson.length(); i++) {
                            JSONObject obj = ObjJson.getJSONObject(i);
                            AuditRoadStore auditRoadStore = new AuditRoadStore();
                            auditRoadStore.setId(obj.getInt("id"));
                            auditRoadStore.setRoad_id(obj.getInt("road_id"));
                            auditRoadStore.setAudit_id(obj.getInt("audit_id"));
                            auditRoadStore.setStore_id(obj.getInt("store_id"));
                            auditRoadStore.setAuditStatus(obj.getInt("audit"));

                            auditRoadsStores.add(i,auditRoadStore);
                        }
                    }
                    Log.d(LOG_TAG, "Ingresado correctamente");
                }else{
                    Log.d(LOG_TAG, "No se ingreso el registro");
                    //return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // return false;
        }
        return  auditRoadsStores;
    }
    /**
     * Obitne  todas las preguntas de un company_id
     * @param company_id
     * @return
     */
    public static ArrayList<Poll> getPolls(int company_id){
        int success ;

        ArrayList<Poll> polls= new ArrayList<Poll>();
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("company_id", String.valueOf(company_id));
            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/admin_api/api_poll.php" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");
            } else{
                success = json.getInt("success");
                if (success > 0) {
                    JSONArray ObjJson;
                    ObjJson = json.getJSONArray("polls");
                    // looping through All Products
                    if(ObjJson.length() > 0) {

                        for (int i = 0; i < ObjJson.length(); i++) {
                            JSONObject obj = ObjJson.getJSONObject(i);
                            Poll poll = new Poll();
                            poll.setId(obj.getInt("id"));
                            poll.setCompany_audit_id(obj.getInt("company_audit_id"));
                            poll.setQuestion(obj.getString("question"));
                            if(obj.isNull("question")) poll.setQuestion("");  else poll.setQuestion(obj.getString("question"));
                            poll.setOrder(obj.getInt("orden"));
                            poll.setSino(obj.getInt("sino"));
                            poll.setOptions(obj.getInt("options"));
                            poll.setOption_type(obj.getInt("option_type"));
                            poll.setMedia(obj.getInt("media"));
                            poll.setComment(obj.getInt("comment"));
                            poll.setComment_requiered(obj.getInt("comment_requiered"));
                            poll.setComentTag(obj.getString("comentTag"));
                            poll.setComentType(obj.getInt("comentType"));
                            poll.setPublicity_id(obj.getInt("publicity"));
                            poll.setCategory_product_id(obj.getInt("categoryProduct"));
                            poll.setProduct_id(obj.getInt("product"));
                            if(obj.isNull("created_at")) poll.setCreated_at("");  else poll.setCreated_at(obj.getString("created_at"));
                            if(obj.isNull("updated_at")) poll.setUpdated_at("");  else poll.setUpdated_at(obj.getString("updated_at"));
                            polls.add(i,poll);
                        }
                    }
                    Log.d(LOG_TAG, "Ingresado correctamente");
                }else{
                    Log.d(LOG_TAG, "No se ingreso el registro");
                    //return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // return false;
        }
        return  polls;
    }

    public static ArrayList<PollOption> getPollOptions(int company_id){
        int success ;

        ArrayList<PollOption> pollOptions= new ArrayList<PollOption>();
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("company_id", String.valueOf(company_id));
            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/admin_api/api_poll_option.php" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");
            } else{
                success = json.getInt("success");
                if (success > 0) {
                    JSONArray ObjJson;
                    ObjJson = json.getJSONArray("poll_options");
                    // looping through All Products
                    if(ObjJson.length() > 0) {
                        for (int i = 0; i < ObjJson.length(); i++) {
                            JSONObject obj = ObjJson.getJSONObject(i);
                            PollOption pollOption = new PollOption();
                            pollOption.setId(obj.getInt("id"));
                            pollOption.setPoll_id(obj.getInt("poll_id"));
                            if(obj.isNull("options")) pollOption.setOptions("");  else pollOption.setOptions(obj.getString("options"));
                            if(obj.isNull("options_abreviado")) pollOption.setOptions_abreviado("");  else pollOption.setOptions_abreviado(obj.getString("options_abreviado"));
                            if(obj.isNull("codigo")) pollOption.setCodigo("");  else pollOption.setCodigo(obj.getString("codigo"));;
                            pollOption.setProduct_id(obj.getInt("product_id"));
                            if(obj.isNull("region")) pollOption.setRegion("");  else pollOption.setRegion(obj.getString("region"));
                            pollOption.setOption_yes_no(obj.getInt("option_yes_no"));
                            pollOption.setComment(obj.getInt("comment"));
                            if(obj.isNull("created_at")) pollOption.setCreated_at("");  else pollOption.setCreated_at(obj.getString("created_at"));;
                            if(obj.isNull("updated_at")) pollOption.setUpdated_at("");  else pollOption.setUpdated_at(obj.getString("updated_at"));;
                            pollOptions.add(i,pollOption);
                        }
                    }
                    Log.d(LOG_TAG, "Ingresado correctamente");
                }else{
                    Log.d(LOG_TAG, "No se ingreso el registro");
                    //return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // return false;
        }
        return  pollOptions;
    }

    public static boolean saveLatLongStore(int store_id, double latitude,double longitude ) {

        int success;
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("id"             , String.valueOf(store_id));
            params.put("latitud"        , String.valueOf(latitude));
            params.put("longitud"       , String.valueOf(longitude));

            JSONParserX jsonParser = new JSONParserX();

            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/updatePositionStore" ,"POST", params);
            Log.d(LOG_TAG, json.toString());

            // json success, tag que retorna el json

            if (json == null) {
                Log.d(LOG_TAG, "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente");
                    return true;
                }else{
                    Log.d(LOG_TAG, "no insertó registro");
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

//    $store_id = $valoresPost['store_id'];
//    $user_id = $valoresPost['user_id'];
//    $company_id = $valoresPost['company_id'];
//    $direccion  = $valoresPost['direccion'];
//    $referencia = $valoresPost['referencia'];
//    $userName = $valoresPost['userName'];
//    $storeName = Input::only('storeName');
//    $comentario = Input::only('comentario');

    /**
     * Cambia la direción de un store
     * @param store_id
     * @param user_id
     * @param company_id
     * @param address
     * @param reference
     * @param userName
     * @param storeName
     * @param comment
     * @return
     */
    public static boolean saveChangeAddressStore(int store_id, int user_id, int company_id, String address, String reference, String userName, String storeName, String comment ) {

        int success;
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("store_id"     , String.valueOf(store_id));
            params.put("user_id"      , String.valueOf(user_id));
            params.put("company_id"   , String.valueOf(company_id));
            params.put("direccion"      , String.valueOf(address));
            params.put("referencia"    , String.valueOf(reference));
            params.put("userName"     , String.valueOf(userName));
            params.put("storeName"    , String.valueOf(storeName));
            params.put("comentario"      , String.valueOf(comment));

            JSONParserX jsonParser = new JSONParserX();

            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/changeAddressStore" ,"POST", params);
            Log.d(LOG_TAG, json.toString());

            // json success, tag que retorna el json

            if (json == null) {
                Log.d(LOG_TAG, "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente");
                    return true;
                }else{
                    Log.d(LOG_TAG, "no insertó registro");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static int newStore(int company_id , Store store) {
        int success;
        int store_id = 0 ;
        try {



            HashMap<String, String> params = new HashMap<>();

            params.put("company_id"                , String.valueOf(company_id));
            params.put("fullname"               , String.valueOf(store.getFullname()));
            params.put("ruc"               , String.valueOf(store.getCadenRuc()));
            params.put("code"                   , String.valueOf(store.getCode()));
//            params.put("distributor"                 , "");
            params.put("distrito"                 , String.valueOf(store.getDistrict()));
            params.put("departamento"                  , String.valueOf(store.getDepartament()));
            params.put("address"                 , String.valueOf(store.getAddress()));
            params.put("phone"                , String.valueOf(store.getPhone()));
            params.put("ejecutivo"                 , String.valueOf(store.getExecutive()));
            params.put("codclient"             , String.valueOf(store.getCodCliente()));
            params.put("giro"             , String.valueOf(store.getGiro()));
            params.put("dex"             , String.valueOf(store.getDex()));
            params.put("contact"             , "");
            params.put("econtact"             , "");
            params.put("pcontact"             , "");


            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            //JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/json/prueba.json" ,"POST", params);
//            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/insertPollPalmeras" ,"POST", params);
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/insertPollPalmeras" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");
                return store_id;
            } else{
                success = json.getInt("success");
                store_id = json.getInt("store_id");
                if (success == 1) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente");
                }else{
                    Log.d(LOG_TAG, "no insertó registro");
                    // return json.getString("message");
                    // return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return store_id;
        }
        return store_id ;
    }

    public static ArrayList<Distributor> getListDistributors(int company_id){
        int success ;

        ArrayList<Distributor> distributors = new ArrayList<Distributor>();

        try {
            HashMap<String, String> params = new HashMap<>();

            params.put("company_id",    String.valueOf(company_id));

            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/getProviders" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    JSONArray ObjJson;
                    ObjJson = json.getJSONArray("regs");
                    // looping through All Products
                    if(ObjJson.length() > 0) {
                        for (int i = 0; i < ObjJson.length(); i++) {
                            JSONObject obj = ObjJson.getJSONObject(i);
                            Distributor distributor = new Distributor();

                            distributor.setId(obj.getInt("user_id"));
                            distributor.setFullName(obj.getString("fullname"));
                            distributor.setCode(obj.getString("codigo"));

                            distributors.add(i,distributor);
                        }
                    }
                    Log.d(LOG_TAG, "Ingresado correctamente");
                }else{
                    Log.d(LOG_TAG, "No se ingreso el registro");
                    //return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // return false;
        }
        return  distributors;
    }

    public static boolean saveOrder(int company_id,int store_id,int product_id, int visit_id, int provider_id,int quantity,String amount,int auditor_id,String price, String code, int type_payment) {

        int success;
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("company_id"         , String.valueOf(company_id));
            params.put("store_id"           , String.valueOf(store_id));
            params.put("auditor_id"         , String.valueOf(auditor_id));
            params.put("product_id"         , String.valueOf(product_id));
            params.put("visit_id"           , String.valueOf(visit_id));
            params.put("provider_id"        , String.valueOf(provider_id));
            params.put("quantity"           , String.valueOf(quantity));
            params.put("amount"             , String.valueOf(amount));
            params.put("price"              , String.valueOf(price));
            params.put("code"               , String.valueOf(code));
            params.put("type_payment"       , String.valueOf(type_payment));

            JSONParserX jsonParser = new JSONParserX();
//            http://ttaudit.com/updateStockMinMax?company_id=153&product_id=690&store_id=37454&visit_id=1&stock_min=2&stock_max=4
//            http://ttaudit.com/saveOrder?company_id=153&product_id=690&store_id=29302&visit_id=2&provider_id=464&quantity=2&amount=367.02&auditor_id=5&price=14.5
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/saveOrder" ,"POST", params);
            Log.d(LOG_TAG, json.toString());

            // json success, tag que retorna el json

            if (json == null) {
                Log.d(LOG_TAG, "Está en nullo");
                return false;
            } else{
                success = json.getInt("success");
                if (success == 1) {
                    Log.d(LOG_TAG, "Se insertó registro correctamente");
                    return true;
                }else{
                    Log.d(LOG_TAG, "no insertó registro");
                    return false;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static ArrayList<Store> getSearchStores(int company_id, String keyword){
        int success ;

        ArrayList<Store> stores= new ArrayList<Store>();
        try {

            HashMap<String, String> params = new HashMap<>();

            params.put("keyword", String.valueOf(keyword));
            params.put("company_id", String.valueOf(company_id));

            JSONParserX jsonParser = new JSONParserX();
            // getting product details by making HTTP request
            //JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/JsonRoadsDetail" ,"POST", params);
            JSONObject json = jsonParser.makeHttpRequest(GlobalConstant.dominio + "/admin_api/api_search_stores.php" ,"POST", params);
            // check your log for json response
            Log.d("Login attempt", json.toString());
            // json success, tag que retorna el json
            if (json == null) {
                Log.d("JSON result", "Está en nullo");

            } else{
                success = json.getInt("success");
                if (success > 0) {
                    JSONArray ObjJson;
                    ObjJson = json.getJSONArray("stores");
                    // looping through All Products
                    if(ObjJson.length() > 0) {

                        for (int i = 0; i < ObjJson.length(); i++) {
                            JSONObject obj = ObjJson.getJSONObject(i);
                            Store store = new Store();
                            store.setId(obj.getInt("id"));
                            store.setRoute_id(0);
                            if(obj.isNull("fullname")) store.setFullname("");  else store.setFullname(obj.getString("fullname"));;
                            if(obj.isNull("cadenaRuc")) store.setCadenRuc("");  else store.setCadenRuc(obj.getString("cadenaRuc"));;
                            if(obj.isNull("documento")) store.setDocument("");  else store.setDocument(obj.getString("documento"));;
                            if(obj.isNull("region")) store.setRegion("");  else store.setRegion(obj.getString("region"));;
                            if(obj.isNull("tipo_bodega")) store.setTypeBodega("");  else store.setTypeBodega(obj.getString("tipo_bodega"));
                            if(obj.isNull("address")) store.setAddress("");  else store.setAddress(obj.getString("address"));
                            if(obj.isNull("district")) store.setDistrict("");  else store.setDistrict(obj.getString("district"));
                            store.setStatus(0);
                            if(obj.isNull("codclient")) store.setCodCliente("");  else store.setCodCliente(obj.getString("codclient"));
                            if(obj.isNull("urbanization")) store.setUrbanization("");  else store.setUrbanization(obj.getString("urbanization"));
                            if(obj.isNull("type")) store.setType("");  else store.setType(obj.getString("type"));
                            if(obj.isNull("ejecutivo")) store.setEjecutivo("");  else store.setEjecutivo(obj.getString("ejecutivo"));
                            if(obj.isNull("latitude")) store.setLatitude(0.0);  else store.setLatitude(obj.getDouble("latitude"));
                            if(obj.isNull("longitude")) store.setLongitude(0.0);  else store.setLongitude(obj.getDouble("longitude"));
                            if(obj.isNull("telephone")) store.setTelephone("");  else store.setTelephone(obj.getString("telephone"));
                            if(obj.isNull("cell")) store.setCell("");  else store.setCell(obj.getString("cell"));
                            if(obj.isNull("comment")) store.setComment("");  else store.setComment(obj.getString("comment"));
                            if(obj.isNull("owner")) store.setOwner("");  else store.setOwner(obj.getString("owner"));
                            if(obj.isNull("fnac")) store.setFnac("");  else store.setFnac(obj.getString("fnac"));


                            stores.add(i,store);
                        }

                    }
                    Log.d(LOG_TAG, "Ingresado correctamente");
                }else{
                    Log.d(LOG_TAG, "No se ingreso el registro");
                    //return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // return false;
        }
        return  stores;
    }

}
