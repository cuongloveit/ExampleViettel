package com.androidtmc.exampleviettel;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.viettel.android.gsm.ViettelClient;
import com.viettel.android.gsm.ViettelError;
import com.viettel.android.gsm.VtResponseCode;
import com.viettel.android.gsm.charging.ChargingGateWayApi;
import com.viettel.android.gsm.charging.PaymentInfo;
import com.viettel.android.gsm.listener.ViettelOnResponse;
import com.viettel.android.gsm.mdm.MdmInfoApi;
import com.viettel.android.gsm.mdm.SmartDeviceInfo;

public class MainActivity extends AppCompatActivity implements ViettelClient.OnConnectionCallbacks {

    private ViettelClient viettelClient;
    private Button btnBuy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Thông tin kết nối với service viettel
        viettelClient = new ViettelClient(this, this);
        // 2 thông số này ta lấy khi tạo ứng dụng
        String publisherId = "112707";
        String appId = "10792";

        viettelClient.setViettelId(publisherId, appId);
        viettelClient.connect();

        //thiết đặt button Mua
        btnBuy = (Button) findViewById(R.id.btnBuy);
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "enter phone number";//
                String purchaseNameId = "quanjean_300k";
                ChargingGateWayApi.processCharging(viettelClient, phoneNumber,purchaseNameId, new ViettelOnResponse<PaymentInfo>() {
                            @Override
                            public void onResult(final PaymentInfo paymentInfo, final int vtCode) {
                                //thanh toán thành công
                                if (vtCode == VtResponseCode.VT_RESULT_OK && paymentInfo != null) {
                                    Toast.makeText(MainActivity.this,paymentInfo != null ? paymentInfo.toString() : "" +
                                            String.valueOf(vtCode), Toast.LENGTH_LONG).show();
                                }
                                //thanh toán thất bại
                                else
                                    Toast.makeText(MainActivity.this ,paymentInfo != null ? paymentInfo.toString() : "" +
                                            String.valueOf(vtCode), Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected() {
        Toast.makeText(this, "connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectFail(ViettelError viettelError) {
        Toast.makeText(this, viettelError.getMessage(), Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viettelClient != null) {
            viettelClient.onDestroy();
        }
    }
}
