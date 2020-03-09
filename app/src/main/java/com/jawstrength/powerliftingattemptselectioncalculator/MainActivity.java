package com.jawstrength.powerliftingattemptselectioncalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText sqRepID = findViewById(R.id.sqRep);
        final EditText sqWeightID = findViewById(R.id.sqWeight);
        final EditText sqRpeID = findViewById(R.id.sqRpe);

        final EditText bnRepID = findViewById(R.id.bnRep);
        final EditText bnWeightID = findViewById(R.id.bnWeight);
        final EditText bnRpeID = findViewById(R.id.bnRpe);

        final EditText dlRepID = findViewById(R.id.dlRep);
        final EditText dlWeightID = findViewById(R.id.dlWeight);
        final EditText dlRpeID = findViewById(R.id.dlRpe);

        final Button btnCalculate = findViewById(R.id.btnCalculate);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sqRepID.getText().toString().isEmpty() ||
                        sqWeightID.getText().toString().isEmpty() ||
                        sqRpeID.getText().toString().isEmpty() ||

                        bnRepID.getText().toString().isEmpty() ||
                        bnWeightID.getText().toString().isEmpty() ||
                        bnRpeID.getText().toString().isEmpty() ||

                        dlRepID.getText().toString().isEmpty() ||
                        dlWeightID.getText().toString().isEmpty() ||
                        dlRpeID.getText().toString().isEmpty())
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setMessage("Please provide all input fields!");
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                    TextView messageText = (TextView)alertDialog.findViewById(android.R.id.message);
                    messageText.setGravity(Gravity.CENTER);

                    Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();

                    //Changing the weight to negative pushes it to the left.
                    layoutParams.weight = 14;
                    btnPositive.setLayoutParams(layoutParams);

                } else {
                    int sqRepInput = Integer.parseInt(sqRepID.getText().toString());
                    int sqWeightInput = Integer.parseInt(sqWeightID.getText().toString());
                    int sqRpeInput = Integer.parseInt(sqRpeID.getText().toString());
                    int sq1RM = oneRepMax(sqRepInput,sqWeightInput, sqRpeInput);
                    int sq1st = firstAttempt(sq1RM);
                    int sq2nd = secondAttempt(sq1RM);
                    int sq3rd = thirdAttempt(sq1RM);

                    int bnRepInput = Integer.parseInt(bnRepID.getText().toString());
                    int bnWeightInput = Integer.parseInt(bnWeightID.getText().toString());
                    int bnRpeInput = Integer.parseInt(bnRpeID.getText().toString());
                    int bn1RM = oneRepMax(bnRepInput,bnWeightInput, bnRpeInput);
                    int bn1st = firstAttempt(bn1RM);
                    int bn2nd = secondAttempt(bn1RM);
                    int bn3rd = thirdAttempt(bn1RM);

                    int dlRepInput = Integer.parseInt(dlRepID.getText().toString());
                    int dlWeightInput = Integer.parseInt(dlWeightID.getText().toString());
                    int dlRpeInput = Integer.parseInt(dlRpeID.getText().toString());
                    int dl1RM = oneRepMax(dlRepInput,dlWeightInput, dlRpeInput);
                    int dl1st = firstAttempt(dl1RM);
                    int dl2nd = secondAttempt(dl1RM);
                    int dl3rd = thirdAttempt(dl1RM);

                    // passing 1rm to resultActivity
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("Squat1st", sq1st);
                    bundle.putInt("Squat2nd", sq2nd);
                    bundle.putInt("Squat3rd", sq3rd);

                    bundle.putInt("Bench1st", bn1st);
                    bundle.putInt("Bench2nd", bn2nd);
                    bundle.putInt("Bench3rd", bn3rd);

                    bundle.putInt("Deadlift1st", dl1st);
                    bundle.putInt("Deadlift2nd", dl2nd);
                    bundle.putInt("Deadlift3rd", dl3rd);

                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    public void openAbout(View view)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

        alertDialog.setMessage("This app is free forever, but it still cost effort to maintain it on AppStore. \n \n Want to buy a cup of coffee?");
        alertDialog.setCancelable(false);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes!!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse("http://paypal.me/akonyein"); // missing 'http://' will cause crashed
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();
        TextView messageText = (TextView)alertDialog.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);

        Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();

        //Changing the weight to negative pushes it to the left.
        layoutParams.weight = 1.5f;
        btnPositive.setLayoutParams(layoutParams);

    }

    public int firstAttempt(int oneRepMax)
    {
        return (int)(oneRepMax * 0.91);
    }

    public int secondAttempt(int oneRepMax)
    {
        return (int)(oneRepMax * 0.96);
    }

    public int thirdAttempt(int oneRepMax)
    {
        return oneRepMax;
    }

    public int oneRepMax(int rep, int weight, int rpe)
    {
        return (int)((weight*((10-(rpe+1))+rep)*0.03)+weight);
    }
}
