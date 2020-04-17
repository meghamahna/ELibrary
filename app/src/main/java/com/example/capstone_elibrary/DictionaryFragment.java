package com.example.capstone_elibrary;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DictionaryFragment extends Fragment {

    String url;
    private TextView showDef;
    private EditText enterWord;
    private Context mContext;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dictionary_frag, container, false);


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        showDef = (TextView) getView().findViewById(R.id.showDef);
        enterWord = (EditText) getView().findViewById(R.id.enterWord);
        button = (Button)getView().findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DictionaryRequest dR = new DictionaryRequest(mContext, showDef);
                url = dictionaryEntries();
                 dR.execute(url);
            }
        });
    }

    private String dictionaryEntries()
    {
        final String language = "en-gb";
        final String word = enterWord.getText().toString(); // now we will get the meaning of the word entered in edittext
        final String fields = "definitions"; // this can be replaced with whatever field you want
        final String strictMatch = "false";
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id + "?" + "fields=" + fields + "&strictMatch=" + strictMatch;
    }



}
