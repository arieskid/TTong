package com.example.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import com.example.voicerecognition.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


/**
 * Created by inhuh on 2015. 5. 21..
 */
public class SignUpView extends View{

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    public SignUpView(final Context context) {
        super(context);

        // get mac address
        WifiManager wifi_mng = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        WifiInfo info = wifi_mng.getConnectionInfo();
        final String mac_addr = info.getMacAddress();

        // get phone #
        TelephonyManager tel_mng = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
        final String phone_num = tel_mng.getLine1Number();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_signup, null, false);

        // show phone number
        /*
        TextView phone = (TextView) view.findViewById(R.id.phone_num);
        phone.setText(phone_num);
        */

        // when the start button clicked!
        Button button_next = (Button) view.findViewById(R.id.button_start);
        button_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int error_check = 0;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ttong", "root", null);
                    PreparedStatement pStmt = conn.prepareStatement("insert into Users values(?,?)");

                    pStmt.setString(1, mac_addr);
                    pStmt.setString(2, phone_num);
                    pStmt.executeUpdate();

                    error_check++;

                    if (error_check > 0) {
                        System.out.println("Safely registered! You can now use TTong!");
                        // go to the friend list page!
                    }
                } catch (Exception e) {
                    System.out.println("Exception : " + e);
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("Sorry, we cannot register now!");
                    alert.show();
                }
            }
        });
    }
}
