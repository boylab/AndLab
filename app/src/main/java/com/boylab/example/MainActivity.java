package com.boylab.example;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.boylab.andlab.utils.DialogUtil;
import com.boylab.andlab.view.RefreshRecycler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DialogUtil.newInstance().init(this, -1);
       findViewById(R.id.btn_01).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
                DialogUtil.newInstance().normalDialog("温馨提示", "是否进行某某操作", new DialogUtil.OnNormalDialogListener() {
                    @Override
                    public void onPositiveClick(DialogInterface dialog, int which) {
                        Log.i("___boylab>>>___", "onPositiveClick: 确认");
                    }

                    @Override
                    public void onNegativeClick(DialogInterface dialog, int which) {
                        Log.i("___boylab>>>___", "onNegativeClick: 取消");
                    }
                });
           }
       });


        findViewById(R.id.btn_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.newInstance().multiDialog("温馨提示", "是否进行某某操作", new DialogUtil.OnMultiDialogListener() {
                    @Override
                    public void onPositiveClick(DialogInterface dialog, int which) {
                        Log.i("___boylab>>>___", "onPositiveClick: 确认");
                    }

                    @Override
                    public void onNeutralClick(DialogInterface dialog, int which) {
                        Log.i("___boylab>>>___", "onNeutralClick: 中立");
                    }

                    @Override
                    public void onNegativeClick(DialogInterface dialog, int which) {
                        Log.i("___boylab>>>___", "onNegativeClick: 取消");
                    }
                });
            }
        });

        findViewById(R.id.btn_03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] items = new String[]{"item0", "item1", "item3", "item4", "item5"};
                DialogUtil.newInstance().listDialog("温馨提示", null, items, new DialogUtil.OnListDialogListener() {
                    @Override
                    public void onListClick(DialogInterface dialog, int which) {
                        Log.i("___boylab>>>___", "onListClick: position = "+ which);
                    }
                });
            }
        });

        findViewById(R.id.btn_04).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] items = new String[]{"item0", "item1", "item3", "item4", "item5"};
                int position = 2;
                DialogUtil.newInstance().singleChoiceDialog("温馨提示", items, position, new DialogUtil.OnSingleChoiceDialogListener() {
                    @Override
                    public void onSingleChoiceClick(DialogInterface dialog, int which) {
                        Log.i("___boylab>>>___", "onSingleChoiceClick: 点击position = "+ which);
                    }
                });
            }
        });

        findViewById(R.id.btn_045).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] items = new String[]{"item0", "item1", "item3", "item4", "item5"};
                boolean[] checkedItems = new boolean[]{false, true, true, false, false};

                DialogUtil.newInstance().multiChoiceDialog("温馨提示", items, checkedItems, new DialogUtil.OnMultiChoiceDialogListener() {
                    @Override
                    public void onMultiChoiceClick(DialogInterface dialog, int which, boolean[] choicesItem) {
                        for (int i = 0; i < choicesItem.length; i++) {
                            Log.i("___boylab>>>___", "onMultiChoiceClick: "+choicesItem[i]);
                        }
                    }
                });
            }
        });

        findViewById(R.id.btn_05).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] items = new String[]{"item0", "item1", "item3", "item4", "item5"};


                DialogUtil.newInstance().waitingDialog("温馨提示", "请稍后...", new DialogUtil.OnWaitingDialogListener() {
                    @Override
                    public void onWaitingDismiss(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(3000); //休眠五秒
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }).start();
                    }
                });
            }
        });

        findViewById(R.id.btn_06).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.newInstance().progressDialog("温馨提示", null, new DialogUtil.OnProgressDialogListener() {
                    @Override
                    public void onProgressUpdate(ProgressDialog dialog) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (int i = 0; i < 11; i++) {
                                    try {
                                        Thread.sleep(500); //休眠五秒
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    int finalI = 10 * i;
                                    MainActivity.this.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (finalI < 100){
                                                dialog.setProgress( finalI);
                                            }else {
                                                dialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                });
            }
        });


        findViewById(R.id.btn_07).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtil.newInstance().inputDialog("温馨提示", "是否进行某某操作", new DialogUtil.OnInputDialogListener() {
                    @Override
                    public void onInputClick(DialogInterface dialog, int which, String inputText) {
                        Log.i("___boylab>>>___", "onInputClick: inputText = "+inputText);
                    }
                });
            }
        });


        findViewById(R.id.btn_08).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View rootView = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_item_test, null);
                TextView textView = rootView.findViewById(R.id.textView);
                DialogUtil.newInstance().showViewDialog("温馨提示", new DialogUtil.OnViewDialogListener() {
                    @Override
                    public View setView() {
                        return rootView;
                    }

                    @Override
                    public void onPositiveClick(DialogInterface dialog, int which) {
                        Log.i("___boylab>>>___", "onPositiveClick: "+textView.getText().toString());
                    }
                });
            }
        });

    }


}