package vn.com.phudnguyen.tools.autovolumemanager.listener.fragment.rule;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import vn.com.phudnguyen.tools.autovolumemanager.R;
import vn.com.phudnguyen.tools.autovolumemanager.listener.database.DatabaseHelper;
import vn.com.phudnguyen.tools.autovolumemanager.listener.utils.GsonUtils;
import vn.com.phudnguyen.tools.autovolumemanager.listener.model.Rule;

import java.util.UUID;

public class RuleDialogFragment extends DialogFragment {

    private static final String TAG = RuleDialogFragment.class.getName();
    private EditText mainText;
    private EditText subText;
    private Rule rule;
    private OnRuleUpdateCompletedListener updateListener;

    public static RuleDialogFragment newInstance() {
        return newInstance(null);
    }

    public static RuleDialogFragment newInstance(Rule rule) {
        RuleDialogFragment frag = new RuleDialogFragment();
        if (rule != null) {
            Bundle args = new Bundle();
            args.putString("rule", GsonUtils.serialize(rule));
            frag.setArguments(args);
        }
        return frag;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.form_add_edit_rule, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainText = view.findViewById(R.id.rule_main_text);
        subText = view.findViewById(R.id.rule_sub_text);
        mainText.requestFocus();

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        Bundle args = getArguments();
        if (args != null && args.containsKey("rule")) {
            rule = GsonUtils.deserizalize(args.getString("rule"), Rule.class);
            getDialog().setTitle("Edit Rule");
        } else {
            rule = Rule.builder().build();
            getDialog().setTitle("Create Rule");
        }
        mainText.setText(rule.getText());
        subText.setText(rule.getSubText());

        view.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "mainText: " + mainText.getText().toString());
                Log.i(TAG, "subText: " + subText.getText().toString());
                try {
                    if (rule.getId() == null) {
                        // new
                        rule.setText(mainText.getText().toString());
                        rule.setSubText(subText.getText().toString());
                        rule.setPackageName("com.spotify.music");
                        rule.setRuleId(UUID.randomUUID().toString());
                        DatabaseHelper.getInstance().insertRule(rule, null);
                    } else {
                        rule.setText(mainText.getText().toString());
                        rule.setSubText(subText.getText().toString());
                        rule.setPackageName("com.spotify.music");
                        DatabaseHelper.getInstance().updateRule(rule, null);
                    }
                    if (getDialog() != null) {
                        getDialog().dismiss();
                    }
                    if (updateListener != null) {
                        updateListener.onCompleted();
                    }
                } catch (Exception ex) {
                    if (updateListener != null) {
                        updateListener.onError(ex);
                    }
                }
            }
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getDialog() != null) {
                    getDialog().dismiss();
                }
            }
        });
    }

    public void setUpdateListener(OnRuleUpdateCompletedListener updateListener) {
        this.updateListener = updateListener;
    }

    public interface OnRuleUpdateCompletedListener {
        void onCompleted();

        void onError(Exception ex);
    }
}
