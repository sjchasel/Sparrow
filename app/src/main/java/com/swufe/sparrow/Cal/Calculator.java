package com.swufe.sparrow.Cal;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import com.swufe.sparrow.R;

public class Calculator extends AppCompatActivity {

    private GridView gridv = null;

    private ArrayAdapter adapter = null;

    private final String[] mTextBtns = new String[]{
            "del","(",")","AC",
            "7","8","9","/",
            "4","5","6","*",
            "1","2","3","+",
            "0",".","=","-",
    };


    EditText result;//结果
    String input = "0";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        result =  findViewById(R.id.edit_input);
        gridv =  findViewById(R.id.grid_buttons);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTextBtns);
        gridv.setAdapter(adapter);
        result.setKeyListener(null);//禁止从键盘输入edit
        gridv.setOnItemClickListener(new OnButtonItemClickListener());
    }


    public float getResult() {
        String string = result.getText().toString();
        float out = (new Cal()).calcu(string);
        return out;
    }

    private class OnButtonItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String text = (String) parent.getAdapter().getItem(position);
            input = result.getText().toString();
            switch (text){
                case "0":
                    result.setText(result.getText().toString()+"0");
                    break;
                case "1":
                    result.setText(result.getText().toString()+"1");
                    break;
                case "2":
                    result.setText(result.getText().toString()+"2");
                    break;
                case "3":
                    result.setText(result.getText().toString()+"3");
                    break;
                case "4":
                    result.setText(result.getText().toString()+"4");
                    break;
                case "5":
                    result.setText(result.getText().toString()+"5");
                    break;
                case "6":
                    result.setText(result.getText().toString()+"6");
                    break;
                case "7":
                    result.setText(result.getText().toString()+"7");
                    break;
                case "8":
                    result.setText(result.getText().toString()+"8");
                    break;
                case "9":
                    result.setText(result.getText().toString()+"9");
                    break;
                case "+":
                    result.setText(result.getText().toString()+"+");
                    break;
                case "-":
                    result.setText(result.getText().toString()+"-");
                    break;
                case ".":
                    result.setText(result.getText().toString()+".");
                    break;
                case "/":
                    result.setText(result.getText().toString()+"/");
                    break;
                case "*":
                    result.setText(result.getText().toString()+"*");
                    break;
                case "=":
                    float f = getResult();
                    result.setText(""+f);
                    break;
                case "AC":
                    result.setText("");
                break;
                case "del":
                    result.setText(input.substring(0, input.length() -1));
                    break;

            }
        }
    }
}
