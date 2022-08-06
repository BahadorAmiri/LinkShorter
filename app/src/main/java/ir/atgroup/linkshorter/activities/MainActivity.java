package ir.atgroup.linkshorter.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.room.Room;

import com.pushpole.sdk.PushPole;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import ir.android.device.HiPerm.HiPerm;
import ir.android.http.HiWeb.HiWeb;
import ir.android.http.HiWeb.OnGet;
import ir.android.http.HiWeb.OnPost;
import ir.atgroup.linkshorter.DaTaBaCe.MyDaTaBaSe;
import ir.atgroup.linkshorter.G;
import ir.atgroup.linkshorter.R;
import ir.atgroup.linkshorter.models.URLs;
import ir.tapsell.sdk.Tapsell;
import ir.tapsell.sdk.TapsellAdRequestListener;
import ir.tapsell.sdk.TapsellAdRequestOptions;
import ir.tapsell.sdk.TapsellAdShowListener;
import ir.tapsell.sdk.TapsellShowOptions;

public class MainActivity extends AppCompatActivity {

    HiWeb hiWeb = new HiWeb();
    AppCompatSpinner main_activity_spinner;
    AppCompatEditText main_activity_edittext;

    MyDaTaBaSe myDaTaBaSe;

    public final String[] WEB_APIs = {"yun.ir", "plink.ir", "7r7.ir", "g02.ir", "vurl.com", "ow.ly", "4h.net", "murl.com", "wq.lt"};
    public final int yun = 0;
    public final int plink = 1;
    public final int r7 = 2;
    public final int g02 = 3;
    public final int vurl = 4;
    public final int owl = 5;
    public final int _4h = 6;
    public final int murl = 7;
    public final int wq = 8;

    String[] permissions = new String[]{Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PushPole.initialize(getApplicationContext(), true);

        HiPerm.onCheckPermissions(this, permissions, (permissionGranted, permissionDenied) -> {
            if (!permissionDenied.isEmpty())
                HiPerm.onRequestPermissions(MainActivity.this, permissions, 0);
        });

        init();
    }


    public void init() {

        main_activity_spinner = findViewById(R.id.main_activity_spinner);
        main_activity_edittext = findViewById(R.id.main_activity_edittext);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, WEB_APIs);
        main_activity_spinner.setAdapter(adapter);
        myDaTaBaSe = Room.databaseBuilder(MainActivity.this, MyDaTaBaSe.class, "mydb").allowMainThreadQueries().build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_option, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @SuppressLint("IntentReset")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.history) {
            startActivity(new Intent(this, HistoryActivity.class));
        } else if (item.getItemId() == R.id.report_bugs) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setData(Uri.parse("email"));
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{G.GMAIL_SENDER});
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            startActivity(Intent.createChooser(intent, "Send Email"));
        } else if (item.getItemId() == R.id.about_me) {
            Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_about);

            View view2 = dialog.findViewById(R.id.edit_name);
            view2.setOnClickListener(view1 -> {

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("email"));
                intent.setType("message/rfc822");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{G.GMAIL_SENDER});
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                startActivity(Intent.createChooser(intent, "Send Email"));

            });

            dialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    public void showMessage(String message) {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_message);
        AppCompatTextView messageBox = dialog.findViewById(R.id.messageBox);
        messageBox.setText(message);
        dialog.show();

    }


    public void shortedLink(String linksh) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_main_shorted_link);
        AppCompatTextView link_txt = dialog.findViewById(R.id.link), linksh_txt = dialog.findViewById(R.id.linksh);
        AppCompatImageView copy_link = dialog.findViewById(R.id.copy_link), copy_linksh = dialog.findViewById(R.id.copy_linksh);

        link_txt.setText(Objects.requireNonNull(main_activity_edittext.getText()).toString());
        linksh_txt.setText(linksh);

        copy_link.setOnClickListener(view -> {
            dialog.dismiss();
            ClipData clipData = ClipData.newPlainText(getString(R.string.app_name), main_activity_edittext.getText().toString());
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(clipData);

            Toast.makeText(this, getString(R.string.main_link_copied), Toast.LENGTH_SHORT).show();

        });

        copy_linksh.setOnClickListener(view -> {
            dialog.dismiss();
            ClipData clipData = ClipData.newPlainText(getString(R.string.app_name), linksh);
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(clipData);

            Toast.makeText(this, getString(R.string.short_link_copied), Toast.LENGTH_SHORT).show();

        });
        dialog.show();
    }


    public Dialog showLoading() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
        return dialog;
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    public void shortLinks(View view) {

        if (Objects.requireNonNull(main_activity_edittext.getText()).toString().startsWith("https://") | main_activity_edittext.getText().toString().startsWith("http://")) {

            if (main_activity_edittext.getText().length() > 10) {
                switch (main_activity_spinner.getSelectedItemPosition()) {

                    case yun:
                        yun();
                        break;

                    case plink:
                        plink();
                        break;

                    case r7:
                        r7();
                        break;

                    case g02:
                        g02();
                        break;

                    case vurl:
                        vurl();
                        break;

                    case owl:
                        owl();
                        break;

                    case _4h:
                        _4h();
                        break;

                    case murl:
                        murl();
                        break;

                    case wq:
                        wq();
                        break;

                }
            } else {
                Toast.makeText(this, getString(R.string.ins_complete), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, getString(R.string.insert_link), Toast.LENGTH_SHORT).show();
        }

    }

    private void wq() {

        JSONObject object = new JSONObject();
        try {
            object.put("sessid", "b45v1skjesbgjoskptag02dql4");

            Dialog dialog = showLoading();

            hiWeb.PostUrlConnection("http://wq.lt/api/", object, new OnPost() {

                @Override
                public void onStart() {
                    dialog.show();
                }

                @Override
                public void onResult(String result) {
                    dialog.dismiss();

                    if (result.startsWith("http://")) {

                        List<URLs> urLsList = new ArrayList<>();
                        urLsList.add(new URLs(Objects.requireNonNull(main_activity_edittext.getText()).toString(), result));

                        myDaTaBaSe.dao().insert(urLsList);

                        shortedLink(result);

                    } else {
                        showMessage(getString(R.string.report_1));
                    }

                }

                @Override
                public void onException(Exception excep) {
                    dialog.dismiss();
                    String str = getString(R.string.net_excep).replace("excep", Objects.requireNonNull(excep.getMessage()));
                    showMessage(str);
                }

                @Override
                public void onError(int errorCode) {
                    dialog.dismiss();
                    String str = getString(R.string.net_err).replace("errorCode", String.valueOf(errorCode));
                    showMessage(str);
                }

                @Override
                public String getBody() {
                    return "submit=true&url=" + Objects.requireNonNull(main_activity_edittext.getText()).toString();
                }
            });

        } catch (JSONException e) {
            showMessage(getString(R.string.report_1));
        }

    }

    private void murl() {

        JSONObject object = new JSONObject();
        try {
            object.put("url", Objects.requireNonNull(main_activity_edittext.getText()).toString());

            Dialog dialog = showLoading();

            hiWeb.GetUrlConnection("https://murl.com/api.php", object, new OnGet() {

                @Override
                public void onStart() {
                    dialog.show();
                }

                @Override
                public void onResult(String result) {
                    dialog.dismiss();

                    if (result.startsWith("https://")) {

                        List<URLs> urLsList = new ArrayList<>();
                        urLsList.add(new URLs(main_activity_edittext.getText().toString(), result));

                        myDaTaBaSe.dao().insert(urLsList);

                        shortedLink(result);

                    } else {
                        showMessage(getString(R.string.report_1));
                    }

                }

                @Override
                public void onException(Exception excep) {
                    dialog.dismiss();
                    String str = getString(R.string.net_excep).replace("excep", Objects.requireNonNull(excep.getMessage()));
                    showMessage(str);
                }

                @Override
                public void onError(int errorCode) {
                    dialog.dismiss();
                    String str = getString(R.string.net_err).replace("errorCode", String.valueOf(errorCode));
                    showMessage(str);
                }

            });

        } catch (JSONException e) {
            showMessage(getString(R.string.report_1));
        }

    }

    private void _4h() {

        JSONObject object = new JSONObject();
        try {
            object.put("url", Objects.requireNonNull(main_activity_edittext.getText()).toString());

            Dialog dialog = showLoading();

            hiWeb.GetUrlConnection("https://4h.net/api/", object, new OnGet() {

                @Override
                public void onStart() {
                    dialog.show();
                }

                @Override
                public void onResult(String result) {
                    dialog.dismiss();

                    if (result.startsWith("https://")) {

                        List<URLs> urLsList = new ArrayList<>();
                        urLsList.add(new URLs(main_activity_edittext.getText().toString(), result));

                        myDaTaBaSe.dao().insert(urLsList);

                        shortedLink(result);

                    } else {
                        showMessage(getString(R.string.report_1));
                    }

                }

                @Override
                public void onException(Exception excep) {
                    dialog.dismiss();
                    String str = getString(R.string.net_excep).replace("excep", Objects.requireNonNull(excep.getMessage()));
                    showMessage(str);
                }

                @Override
                public void onError(int errorCode) {
                    dialog.dismiss();
                    String str = getString(R.string.net_err).replace("errorCode", String.valueOf(errorCode));
                    showMessage(str);
                }

            });

        } catch (JSONException e) {
            showMessage(getString(R.string.report_1));
        }

    }

    private void owl() {

        JSONObject data = new JSONObject();
        try {
            data.put("apiKey", "x1yzoctuClmbaFunEJ4q5");
            data.put("longUrl", main_activity_edittext.getText());

            Dialog dialog = showLoading();

            hiWeb.GetUrlConnection("http://ow.ly/api/1.1/url/shorten", data, new OnGet() {

                @Override
                public void onStart() {
                    dialog.show();
                }

                @Override
                public void onResult(String result) {
                    dialog.dismiss();

                    try {

                        JSONObject object = new JSONObject(result);
                        JSONObject results = object.getJSONObject("results");

                        String urlsh = results.getString("shortUrl");

                        List<URLs> urLsList = new ArrayList<>();
                        urLsList.add(new URLs(Objects.requireNonNull(main_activity_edittext.getText()).toString(), urlsh));

                        myDaTaBaSe.dao().insert(urLsList);

                        shortedLink(urlsh);

                    } catch (JSONException e) {
                        showMessage(getString(R.string.report_1));
                    }

                }

                @Override
                public void onException(Exception excep) {
                    dialog.dismiss();
                    String str = getString(R.string.net_excep).replace("excep", Objects.requireNonNull(excep.getMessage()));
                    showMessage(str);
                }

                @Override
                public void onError(int errorCode) {
                    dialog.dismiss();
                    String str = getString(R.string.net_err).replace("errorCode", String.valueOf(errorCode));
                    showMessage(str);
                }

            });


        } catch (JSONException e) {
            showMessage(getString(R.string.report_1));
        }


    }

    private void vurl() {

        JSONObject object = new JSONObject();
        try {
            object.put("url", Objects.requireNonNull(main_activity_edittext.getText()));

            Dialog dialog = showLoading();

            hiWeb.GetUrlConnection("https://vurl.com/api.php", object, new OnGet() {

                @Override
                public void onStart() {
                    dialog.show();
                }

                @Override
                public void onResult(String result) {
                    dialog.dismiss();

                    if (result.startsWith("https://")) {

                        List<URLs> urLsList = new ArrayList<>();
                        urLsList.add(new URLs(main_activity_edittext.getText().toString(), result));

                        myDaTaBaSe.dao().insert(urLsList);

                        shortedLink(result);

                    } else {
                        showMessage(getString(R.string.report_1));
                    }


                }

                @Override
                public void onException(Exception excep) {
                    dialog.dismiss();
                    String str = getString(R.string.net_excep).replace("excep", Objects.requireNonNull(excep.getMessage()));
                    showMessage(str);
                }

                @Override
                public void onError(int errorCode) {
                    dialog.dismiss();
                    String str = getString(R.string.net_err).replace("errorCode", String.valueOf(errorCode));
                    showMessage(str);
                }
            });

        } catch (JSONException e) {
            showMessage(getString(R.string.report_1));
        }

    }

//    private void is() {
//
//        JSONObject object = new JSONObject();
//        try {
//            object.put("link", Objects.requireNonNull(main_activity_edittext.getText()).toString());
//
//            hiWeb.GetUrlConnection("http://taimaz-team.ir/apps/linkshorter/short.php", object, new HiWebListener.GET() {
//                final Dialog dialog = showLoading();
//
//                @Override
//                public void onStart() {
//                    dialog.show();
//                }
//
//                @Override
//                public void onResult(String result) {
//
//                    dialog.dismiss();
//
//
//                }
//
//                @Override
//                public void onException(Exception excep) {
//                    dialog.dismiss();
//                    String str = getString(R.string.net_excep).replace("excep", Objects.requireNonNull(excep.getMessage()));
//                    showMessage(str);
//                }
//
//                @Override
//                public void onError(int errorCode) {
//                    dialog.dismiss();
//                    String str = getString(R.string.net_err).replace("errorCode", String.valueOf(errorCode));
//                    showMessage(str);
//                }
//
//            });
//
//        } catch (JSONException e) {
//            showMessage(getString(R.string.report_1));
//        }
//
//    }

    public void g02() {

        JSONObject object = new JSONObject();
        try {
            object.put("link", Objects.requireNonNull(main_activity_edittext.getText()).toString());

            hiWeb.GetUrlConnection("http://g02.ir/api/v1/public/", object, new OnGet() {
                final Dialog dialog = showLoading();

                @Override
                public void onStart() {
                    dialog.show();
                }

                @Override
                public void onResult(String result) {
                    dialog.dismiss();
                    try {
                        JSONObject json = new JSONObject(result);

                        String urlsh = json.getString("full_short_link");

                        List<URLs> urLsList = new ArrayList<>();
                        urLsList.add(new URLs(main_activity_edittext.getText().toString(), urlsh));

                        myDaTaBaSe.dao().insert(urLsList);

                        shortedLink(urlsh);

                    } catch (JSONException e) {
                        showMessage(getString(R.string.report_1));
                    }
                }

                @Override
                public void onException(Exception excep) {
                    dialog.dismiss();
                    String str = getString(R.string.net_excep).replace("excep", Objects.requireNonNull(excep.getMessage()));
                    showMessage(str);
                }

                @Override
                public void onError(int errorCode) {
                    dialog.dismiss();
                    String str = getString(R.string.net_err).replace("errorCode", String.valueOf(errorCode));
                    showMessage(str);
                }
            });

        } catch (JSONException e) {
            showMessage(getString(R.string.report_1));
        }

    }

    private void r7() {
        hiWeb.PostUrlConnection("http://7r7.ir/api/make", null, new OnPost() {

            final Dialog dialog = showLoading();

            @Override
            public void onStart() {
                dialog.show();
            }

            @Override
            public void onResult(String result) {
                dialog.dismiss();
                try {
                    JSONObject object = new JSONObject(result);

                    if (object.getInt("status") == 2) {

                        String urlsh = "http://7r7.ir/" + object.getString("s_url");

                        List<URLs> urLsList = new ArrayList<>();
                        urLsList.add(new URLs(Objects.requireNonNull(main_activity_edittext.getText()).toString(), urlsh));

                        myDaTaBaSe.dao().insert(urLsList);

                        shortedLink(urlsh);

                    } else {
                        showMessage(getString(R.string.report_1));
                    }

                } catch (JSONException e) {
                    showMessage(getString(R.string.report_1));
                }
            }

            @Override
            public void onException(Exception excep) {
                dialog.dismiss();
                String str = getString(R.string.net_excep).replace("excep", Objects.requireNonNull(excep.getMessage()));
                showMessage(str);
            }

            @Override
            public void onError(int errorCode) {
                dialog.dismiss();
                String str = getString(R.string.net_err).replace("errorCode", String.valueOf(errorCode));
                showMessage(str);
            }

            @Override
            public String getBody() {
                return "url=" + Objects.requireNonNull(main_activity_edittext.getText()).toString();
            }
        });
    }

    private void plink() {

        JSONObject object = new JSONObject();
        try {
            object.put("api", "gVi1o7fM9rqU");
            object.put("url", Objects.requireNonNull(main_activity_edittext.getText()).toString());
            hiWeb.GetUrlConnection("https://plink.ir/api/", object, new OnGet() {
                final Dialog dialog = showLoading();

                @Override
                public void onStart() {
                    dialog.show();
                }

                @Override
                public void onResult(String result) {
                    dialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(result);

                        if (jsonObject.getInt("error") == 0) {

                            String urlsh = jsonObject.getString("short");

                            List<URLs> urLsList = new ArrayList<>();
                            urLsList.add(new URLs(main_activity_edittext.getText().toString(), urlsh));

                            myDaTaBaSe.dao().insert(urLsList);

                            shortedLink(urlsh);

                        } else {
                            showMessage(getString(R.string.report_1));
                        }

                    } catch (JSONException e) {
                        showMessage(getString(R.string.report_1));
                    }

                }

                @Override
                public void onError(int errorCode) {
                    dialog.dismiss();
                    String str = getString(R.string.net_err).replace("errorCode", String.valueOf(errorCode));
                    showMessage(str);
                }

                @Override
                public void onException(Exception excep) {
                    dialog.dismiss();
                    String str = getString(R.string.net_excep).replace("excep", Objects.requireNonNull(excep.getMessage()));
                    showMessage(str);
                }
            });
        } catch (JSONException e) {
            showMessage(getString(R.string.report_1));
        }

    }

    private void yun() {

        hiWeb.PostUrlConnection("https://yun.ir/api/v1/urls", null, new OnPost() {
            final Dialog dialog = showLoading();

            @Override
            public void onStart() {
                dialog.show();
            }

            @Override
            public void onResult(String result) {
                dialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("success")) {

                        JSONObject doc = jsonObject.getJSONObject("doc");
                        String urlsh = doc.getString("url");

                        List<URLs> urLsList = new ArrayList<>();
                        urLsList.add(new URLs(Objects.requireNonNull(main_activity_edittext.getText()).toString(), urlsh));

                        myDaTaBaSe.dao().insert(urLsList);

                        shortedLink(urlsh);

                    } else {
                        showMessage(getString(R.string.report_1));
                    }
                } catch (JSONException e) {
                    showMessage(getString(R.string.report_1));
                }
            }

            @Override
            public void onError(int errorCode) {
                dialog.dismiss();
                String str = getString(R.string.net_err).replace("errorCode", String.valueOf(errorCode));
                showMessage(str);
            }

            @Override
            public void onException(Exception excep) {
                dialog.dismiss();
                String str = getString(R.string.net_excep).replace("excep", Objects.requireNonNull(excep.getMessage()));
                showMessage(str);
            }

            @Override
            public String getBody() {
                return "{ \"url\": \"" + Objects.requireNonNull(main_activity_edittext.getText()).toString() + "\", \"name\": \"" + getString(R.string.app_name) + "\" , \"premium\":\" 1\" }";
            }

            @Override
            public Map<String, String> getHeader() {

                Map<String, String> map = new HashMap<>();
                map.put("Content-Type", "application/json");
                map.put("X-API-KEY", "293:rw4c7hbfju8oosckgsgwwswco8wogok");
                return map;
            }
        });

    }

//    private void ad2() {
//
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("url", Objects.requireNonNull(main_activity_edittext.getText()).toString());
//            jsonObject.put("api", "0898acfb97a58834aeda58d720918b25eeba209c");
//            hiWeb.GetUrlConnection("https://2ad.ir/api", jsonObject, new HiWebListener.GET() {
//                final Dialog dialog = showLoading();
//
//                @Override
//                public void onStart() {
//                    dialog.show();
//                }
//
//                @Override
//                public void onResult(String result) {
//                    dialog.dismiss();
//                    try {
//
//                        JSONObject data = new JSONObject(result);
//
//                        if (data.getString("status").equals("success")) {
//
//                            String urlsh = data.getString("shortenedUrl");
//
//                            List<URLs> urLsList = new ArrayList<>();
//                            urLsList.add(new URLs(main_activity_edittext.getText().toString(), urlsh));
//
//                            myDaTaBaSe.dao().insert(urLsList);
//
//                            shortedLink(urlsh);
//
//                        } else {
//                            showMessage(getString(R.string.report_1));
//                        }
//
//                    } catch (JSONException e) {
//                        showMessage(getString(R.string.report_1));
//                    }
//
//                }
//
//                @Override
//                public void onError(int errorCode) {
//                    dialog.dismiss();
//                    String str = getString(R.string.net_err).replace("errorCode", String.valueOf(errorCode));
//                    showMessage(str);
//                }
//
//                @Override
//                public void onException(Exception excep) {
//                    dialog.dismiss();
//                    String str = getString(R.string.net_excep).replace("excep", Objects.requireNonNull(excep.getMessage()));
//                    showMessage(str);
//                }
//
//            });
//        } catch (JSONException e) {
//            showMessage(getString(R.string.report_1));
//        }
//
//    }

    public void showTab(View view) {

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_show_tab);

        Button btn_tamasha = dialog.findViewById(R.id.btn_confirm);
        btn_tamasha.setOnClickListener(view1 -> {

            dialog.dismiss();

            TapsellAdRequestOptions options = new TapsellAdRequestOptions();
            options.setCacheType(TapsellAdRequestOptions.CACHE_TYPE_CACHED);

            Tapsell.requestAd(MainActivity.this, G.TAPSELL_ZONE_ID, options, new TapsellAdRequestListener() {
                @Override
                public void onAdAvailable(String adID) {

                    TapsellShowOptions showOptions = new TapsellShowOptions();
                    showOptions.setBackDisabled(true);

                    Tapsell.showAd(MainActivity.this, G.TAPSELL_ZONE_ID, adID, showOptions, new TapsellAdShowListener() {

                        @Override
                        public void onRewarded(boolean b) {
                            Toast.makeText(MainActivity.this, getString(R.string.tnx), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String s) {
                            Toast.makeText(MainActivity.this, getString(R.string.tnx), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onClosed() {
                            Toast.makeText(MainActivity.this, getString(R.string.tnx), Toast.LENGTH_SHORT).show();
                        }
                    });


                }

                @Override
                public void onError(String s) {
                    Toast.makeText(MainActivity.this, getString(R.string.tnx), Toast.LENGTH_SHORT).show();
                }
            });

        });

        dialog.show();

    }

}