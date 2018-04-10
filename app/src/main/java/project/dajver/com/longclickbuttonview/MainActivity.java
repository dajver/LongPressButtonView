package project.dajver.com.longclickbuttonview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.dajver.com.longclickbuttonview.view.ButtonView;

public class MainActivity extends AppCompatActivity implements ButtonView.OnButtonLongPressReachedEndListener {

    @BindView(R.id.buttonView)
    ButtonView buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        buttonView.setOnButtonLongPressReachedEndListener(this);
    }

    @Override
    public void onButtonLongPressReachedEnd() {
        Toast.makeText(this, getString(R.string.you_ended_this_button), Toast.LENGTH_LONG).show();
    }
}
