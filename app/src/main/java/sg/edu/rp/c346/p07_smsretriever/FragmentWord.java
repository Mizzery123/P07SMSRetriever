package sg.edu.rp.c346.p07_smsretriever;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentWord extends Fragment {

    EditText etWord;
    Button btn, btnEmail;
    TextView tvWord;
    String smsBody;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_word, container, false);

        etWord = view.findViewById(R.id.etWord);
        btn = view.findViewById(R.id.btnWord);
        tvWord = view.findViewById(R.id.tvWord);
        btnEmail = view.findViewById(R.id.btnEmail);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = etWord.getText().toString();
                String[] inter = word.trim().split(" ");

                Uri uri = Uri.parse("content://sms");
                String[] reqCols = new String[]{"date", "address", "body", "type"};
                ContentResolver cr = getActivity().getContentResolver();
                smsBody = "";

                for (int i=0; i<inter.length; i++){
                    String filter = "body LIKE ?";

                    String[] filterArgs = {"%" + inter[i] + "%"};

                    Cursor cursor = cr.query(uri, reqCols, filter, filterArgs, null);

                    if (cursor.moveToFirst()){
                        do{
                            long dateInMillis = cursor.getLong(0);
                            String date = (String) DateFormat.format("dd MMM yyyy h:mm:ss aa", dateInMillis);
                            String address = cursor.getString(1);
                            String body = cursor.getString(2);
                            String type = cursor.getString(3);
                            if (type.equalsIgnoreCase("1")){
                                type = "Inbox:";
                            } else{
                                type = "Sent:";
                            }
                            smsBody += type + " " + address + "\n at " + date + "\n\"" + body + "\"\n\n";
                        } while(cursor.moveToNext());
                    }
                }
                tvWord.setText(smsBody);
            }
        });



        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"jeremysunpokes@gmail.com"});
                email.putExtra(Intent.EXTRA_SUBJECT, "Hello");
                email.putExtra(Intent.EXTRA_TEXT, smsBody);

                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose client: "));


            }
        });
        return view;
    }

}
