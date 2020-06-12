package com.swufe.sparrow.Cal;

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


public class Calculator extends AppCompatActivity {

    private GridView mGridView = null;//操作按钮

    //private EditText mEditInput = null;//输入框

    private ArrayAdapter mAdapter = null;//适配器

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
    private void getResult() {


        String exp = main_et_result.getText().toString();
        if (exp == null || exp.equals(""))
            return;
        if (!exp.contains(""))
            return;
        if (clear_flag) {
            clear_flag = false;
            return;
        }
        clear_flag = true;
        double result = 0;
        //运算符前的数字
        String s1 = exp.substring(0, exp.indexOf(" "));
        //运算符
        String op = exp.substring(exp.indexOf(" ") + 1, exp.indexOf(" ") + 2);
        //运算符后的数字
        String s2 = exp.substring(exp.indexOf(" ") + 3);
        if (!s1.equals("") && !s2.equals("")) {
            double d1 = Double.parseDouble(s1);
            double d2 = Double.parseDouble(s2);
            if (op.equals("+")) {
                result = d1 + d2;
            } else if (op.equals("-")) {
                result = d1 - d2;
            } else if (op.equals("*")) {
                result = d1 * d2;
            } else if (op.equals("/")) {
                if (d2 == 0) {
                    result = 0;
                } else {
                    result = d1 / d2;
                }
            }
            if (!s1.contains(".") && !s2.contains(".") && !op.equals("/")) {
                int r = (int) result;
                main_et_result.setText(r + "");
            } else {
                main_et_result.setText(result + "");
            }
        } else if (!s1.equals("") && s2.equals("")) {
            main_et_result.setText(exp);
        } else if (s1.equals("") && !s2.equals("")) {
            double d2 = Double.parseDouble(s2);
            if (op.equals("+")) {
                result = 0 + d2;
            } else if (op.equals("-")) {
                result = 0 - d2;
            } else if (op.equals("*")) {
                result = 0;
            } else if (op.equals("/")) {
                result = 0;
            }
            if (!s1.contains(".") && !s2.contains(".")) {
                int r = (int) result;
                main_et_result.setText(r + "");
            } else {
                main_et_result.setText(result + "");
            }
        } else {
            main_et_result.setText("");
        }
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
                    getResult();
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
