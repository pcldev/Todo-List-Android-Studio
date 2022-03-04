package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Database database;
    ListView lvCongViec;
    ArrayList<CongViec> lstCongViec;
    CongViecAdapter congViecAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCongViec = findViewById(R.id.lvCongViec);
        lstCongViec = new ArrayList<>();

        congViecAdapter = new CongViecAdapter(this,R.layout.cong_viec,lstCongViec);
        lvCongViec.setAdapter(congViecAdapter);

        // tao db
        database = new Database(this,"ghichu.sqlite",null,1);

        // tao bang CongViec
        //database.QueryData("CREATE TABLE IF NOT EXISTS CongViec(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenCV VARCHAR(200))");

        // insert data
        //database.QueryData("INSERT INTO CongViec VALUES(null,'Làm bài tập android')");
        GetDataCongViec();

    }

    private void GetDataCongViec(){
        // select data
        Cursor dataCongViec = database.GetData("SELECT * FROM CongViec");
        // clear data cua array
        lstCongViec.clear();

        while (dataCongViec.moveToNext()){
            String ten = dataCongViec.getString(1);// do id la so 0 nen muon lay noi dung thi lay so 1
            int id= dataCongViec.getInt(0);
            lstCongViec.add(new CongViec(id,ten));
        }
        congViecAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_congviec,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuAdd){
            DialogThem();
        }
        return super.onOptionsItemSelected(item);
    }

    private void DialogThem(){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog);

        EditText editTen = dialog.findViewById(R.id.edTxtTenCV);
        Button btnThem = (Button) dialog.findViewById(R.id.btnThem);
        Button btnHuy = (Button) dialog.findViewById(R.id.btnHuy);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenCv= editTen.getText().toString();
                if(tenCv.trim() == ""){
                    Toast.makeText(MainActivity.this, "Vui long nhap cong viec", Toast.LENGTH_SHORT).show();
                }else{
                    database.QueryData("INSERT INTO CongViec VALUES(null,'"+tenCv+"')");
                    Toast.makeText(MainActivity.this, "Them thanh cong", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetDataCongViec();
                }
            }
        });

        dialog.show();
    }

    public void DialogSuaCongViec(String ten,int id){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_sua);

        EditText editTenCV = dialog.findViewById(R.id.edTenEdit);
        Button btnXacNhan = dialog.findViewById(R.id.btnXacNhan);
        Button btnHuy = dialog.findViewById(R.id.btnHuyEdit);

        editTenCV.setText(ten);

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenMoi = editTenCV.getText().toString().trim();
                database.QueryData("UPDATE CongViec SET TenCV = '"+tenMoi+"' WHERE Id = '"+id+"'");
                Toast.makeText(MainActivity.this, "Cap nhat thanh cong", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                GetDataCongViec();
            }
        });

        dialog.show();
    }

    public void XoaCongViec(int id,String tenCv){
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có muốn công việc " +tenCv + " không?");
        dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database.QueryData("DELETE FROM CongViec WHERE Id = '"+id+"'" );
                Toast.makeText(MainActivity.this, "Đã xóa " + tenCv, Toast.LENGTH_SHORT).show();
                GetDataCongViec();
            }
        });

        dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        dialogXoa.show();
    }
}