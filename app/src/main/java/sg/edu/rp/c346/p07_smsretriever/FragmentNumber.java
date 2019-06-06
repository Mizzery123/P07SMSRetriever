package sg.edu.rp.c346.p07_smsretriever;


import android.Manifest;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.PermissionChecker;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentNumber extends Fragment {

    Button btn;
    EditText etNum;
TextView tvShow;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_number, container, false);

        etNum = view.findViewById(R.id.etNum);
        btn = view.findViewById(R.id.btnNum);
        tvShow = view.findViewById(R.id.tvShow);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    Uri uri = Uri.parse("content://sms");
                    String[] reqCols = new String[]{"date", "address", "body", "type"};
                    String filter = "address LIKE ?";
                    String[] args = {"%"+etNum.getText().toString()+"%"};
                    ContentResolver cr = getActivity().getContentResolver();
                    Cursor cursor = cr.query(uri, reqCols, filter, args, null);
                    String smsBody = "";
                    if (cursor.moveToFirst()){
                        do{
                            android.text.format.DateFormat df = new android.text.format.DateFormat();
                            long dateInMillis = cursor.getLong(0);
                            String date = (String)df.format("dd MMM yyyy h:mm:ss aa", dateInMillis);

                            String address = cursor.getString(1);
                            String body = cursor.getString(2);
                            String type = cursor.getString(3);
                            if (type.equalsIgnoreCase("1")){
                                type = "Inbox:";

                            }
                            smsBody += type + " " + address + "\n at " + date + "\n\"" + body + "\"\n\n";
                        }while (cursor.moveToNext());
                    }
                    tvShow.setText(smsBody);


            }
        });

        return view;

    }

}
