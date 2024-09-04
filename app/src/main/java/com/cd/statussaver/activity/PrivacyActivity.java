package com.cd.statussaver.activity;

import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cd.statussaver.R;


public class PrivacyActivity extends AppCompatActivity {

    TextView privacyText;
    String htmlText;
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();

            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        //toolbar = findViewById(R.id.toolbarPrivacy);
        privacyText = findViewById(R.id.privacyText);
        htmlText = "        <h3> Privacy Policy </h3>\n" +
                " <p>Privacy Policy\n" +
                "Master Toolkit built the Status Saver app as a Free app. This SERVICE is provided by Master Toolkit at no cost and is intended for use as is. This page is used to inform visitors regarding my policies with the collection, use, and disclosure of Personal Information if anyone decided to use my Service. If you choose to use my Service, then you agree to the collection and use of information in relation to this policy. The Personal Information that We collect is used for providing and improving the Service. We will not use or share your information with anyone except as described in this Privacy Policy.\n</p>\n" +
                "        <h3>Information Collection and Use</h3>\n" +
                "        <p>To enhance your usage of our Service, it may be necessary for you to furnish us with specific personally identifiable details. Any data requested will remain stored on your device and will not be gathered by us through any means..\n" +
                "           Our app utilizes third-party services which might gather information for identification purposes.\n" +
                "Here is the link to the privacy policies of the third-party service providers utilized by the app.\n" +
                "            <br/>\u2022 Google Play Services\n" +
                "            <br/>\u2022 AdMob\n" +
                "            <br/>\u2022 Firebase Analytics\n" +
                "            <br/>\u2022 Firebase Crashlytics\n" +
                "        </p>\n" +
                "        <h3> Log Data</h3>\n" +
                "        <p>We want to notify you that whenever you utilize my Service, if an error occurs in the app, We gather data and information (via third-party products) on your device known as Log Data. This Log Data might encompass details such as your device's Internet Protocol (“IP”) address, device name, operating system version, the configuration of the app during the use of my Service, the timestamp of your Service usage, and other relevant statistics..\n.</p>\n" +
                "        <h3> Cookies </h3>\n" +
                "        <p> Cookies are files containing a small amount of data. They serve as anonymous unique identifiers and are often employed by websites. These files are transmitted to your browser when you visit websites and are stored in your device's internal memory.\n" +
                "\n" +
                "            This Service does not directly utilize these \"cookies.\" Nonetheless, it may incorporate third-party code and libraries that employ \"cookies\" to gather data and enhance their services. You retain the choice to accept or decline these cookies and receive notifications when they are sent to your device. Declining our cookies may restrict your access to certain parts of this Service</p>\n" +
                "        <h3> Service Providers </h3>\n" +
                "        <p>I may employ third-party companies and individuals due to the following reasons:\n" +
                "            <br/>\u2022 Streamlining our Service\n" +
                "            <br/>\u2022  Delivering the Service on our behalf;\n" +
                "            <br/>\u2022 Conducting Service-related tasks.;\n" +
                "            <br/>\u2022 Assisting in analyzing the usage of our Service.\n" +
                "            Users should be aware that these third parties may access their Personal Information. This access is solely for the purpose of fulfilling assigned tasks on our behalf. However, they are bound by obligation not to disclose or utilize the information for any other intent." +
                "\n" +
                "        <h3>Security</h3>\n" +
                "        <p>Your trust in providing us with your Personal Information is highly valued. We endeavor to employ commercially acceptable methods to safeguard it. However, it's important to note that no transmission over the internet or electronic storage method can be guaranteed to be 100% secure and reliable.</p>\n" +
                "        <h3>Links to Other Sites</h3>\n" +
                "        <p>When you use this Service, you may encounter links leading to other websites. Clicking on a third-party link will take you to that particular site. It's important to understand that these external sites are not under my operation. Consequently, We highly recommend that you carefully review the Privacy Policy of these websites. We have no authority over them and do not take responsibility for their content, privacy policies, or practices.</p>\n" +
                "        <h3>Children’s Privacy</h3>\n" +
                "        <p>Our services are not intended for individuals under the age of 13. We do not knowingly gather personally identifiable information from children under 13 years old. Should We become aware that a child under 13 has provided me with personal information, We will promptly remove it from our servers. If you are a parent or guardian who knows that your child has provided us with personal information, please reach out to me so that We can take appropriate actions.</p>\n" +
                "        <h3>Changes to This Privacy Policy</h3>\n" +
                "        <p>Periodically, We may revise our Privacy Policy. Therefore, it's recommended that you check this page regularly for updates. We will inform you of any modifications by publishing the revised Privacy Policy on this page.</p>\n" +
                "        <h3>Contact Us</h3>\n" +
                "        <p>Please feel free to reach out to me at mysat2021@gmail.com if you have any questions or suggestions regarding my Privacy Policy.</p>";
        privacyText.setText(Html.fromHtml(htmlText));
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                privacyText.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
            }
        }catch (Exception e)
        {

        }
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PrivacyActivity.this, AboutUsActivity.class);
//                finish();
//                startActivity(intent);
//            }
//        });
   }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
