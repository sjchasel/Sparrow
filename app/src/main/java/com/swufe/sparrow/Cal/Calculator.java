package com.swufe.sparrow.Cal;
import com.swufe.sparrow.Cal.Cal;
        import android.os.Bundle;
        import android.text.Html;
        import android.view.View;
        import android.view.Window;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.GridView;
        import android.widget.Toast;

        import androidx.appcompat.app.AppCompatActivity;

        import com.swufe.sparrow.R;

import com.swufe.sparrow.Cal.Cal;

import static com.swufe.sparrow.Cal.Cal.calrp;
import static com.swufe.sparrow.Cal.Cal.getrp;

public class Calculator extends AppCompatActivity {

    private GridView mGridView = null;

    private ArrayAdapter mAdapter = null;

    private final String[] mTextBtns = new String[]{
            "del","(",")","AC",
            "7","8","9","/",
            "4","5","6","*",
            "1","2","3","+",
            "0",".","=","-",
    };


    EditText main_et_result;//结果
    boolean clear_flag = false;//清空
    String input = "0";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        main_et_result =  findViewById(R.id.edit_input);
        mGridView =  findViewById(R.id.grid_buttons);
        // 创建适配器
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTextBtns);
        // 设置适配器
        mGridView.setAdapter(mAdapter);

        // 禁止EditText从键盘输入
        main_et_result.setKeyListener(null);

        // 为操作按钮设置按键监听
        mGridView.setOnItemClickListener(new OnButtonItemClickListener());
    }

    /**
     * 这个函数用于设置EditText的显示内容,主要是为了加上html标签.
     * 所有的显示EditText内容都需要调用此函数
     */
    private void setText() {
        // 设置光标在EditText的最后位置
        main_et_result.setSelection(main_et_result.getText().length());
    }

    /**
     * 执行 待计算表达式,当用户按下 = 号时,调用这个方法
     */
    private float getResult() {
        String string = main_et_result.getText().toString();
        Float out = calrp(getrp(string));
        //String exp = main_et_result.getText().toString();
        return out;
    }




    private class OnButtonItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String text = (String) parent.getAdapter().getItem(position);
            input = main_et_result.getText().toString();
            switch (text){
                case "0":
                    main_et_result.setText(main_et_result.getText().toString()+"0");
                    break;
                case "1":
                    main_et_result.setText(main_et_result.getText().toString()+"1");
                    break;
                case "2":
                    main_et_result.setText(main_et_result.getText().toString()+"2");
                    break;
                case "3":
                    main_et_result.setText(main_et_result.getText().toString()+"3");
                    break;
                case "4":
                    main_et_result.setText(main_et_result.getText().toString()+"4");
                    break;
                case "5":
                    main_et_result.setText(main_et_result.getText().toString()+"5");
                    break;
                case "6":
                    main_et_result.setText(main_et_result.getText().toString()+"6");
                    break;
                case "7":
                    main_et_result.setText(main_et_result.getText().toString()+"7");
                    break;
                case "8":
                    main_et_result.setText(main_et_result.getText().toString()+"8");
                    break;
                case "9":
                    main_et_result.setText(main_et_result.getText().toString()+"9");
                    break;
                case "+":
                    main_et_result.setText(main_et_result.getText().toString()+"+");
                    break;
                case "-":
                    main_et_result.setText(main_et_result.getText().toString()+"-");
                    break;
                case ".":
                    main_et_result.setText(main_et_result.getText().toString()+".");
                    if(clear_flag){
                        clear_flag = false;
                        main_et_result.setText("");
                    }
                    main_et_result.setText(input+((Button) view).getText().toString());
                    break;
                case "/":
                    main_et_result.setText(main_et_result.getText().toString()+"/");
                    if(clear_flag){
                        clear_flag = false;
                        input = "";
                        main_et_result.setText("");
                    }
                    //main_et_result.setText(input+((Button) view).getText().toString());
                    break;
                case "*":
                    main_et_result.setText(main_et_result.getText().toString()+"*");
                    break;
                case "=":
                    float f = getResult();
                    main_et_result.setText(""+f);
                    break;
                case "AC":
                    main_et_result.setText("c");
                    clear_flag = false;
                    input = "";
                    main_et_result.setText("");
                break;
                case "del":
                    main_et_result.setText("del");
                    if(clear_flag){
                        clear_flag = false;
                        main_et_result.setText("");
                    }else if(input != null || !input.equals("")){
                        main_et_result.setText(input.substring(0,input.length()-1));
                    }
                    break;

            }
        }
    }
}
