package com.hualing.htk_merchant.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.hualing.htk_merchant.R;
import com.hualing.htk_merchant.adapter.BillRecordAdapter;

import butterknife.BindView;

public class BillRecordActivity extends BaseActivity {

    @BindView(R.id.billRecord_lv)
    ListView billRecordLV;
    private BillRecordAdapter billRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initLogic() {
        billRecordAdapter = new BillRecordAdapter(BillRecordActivity.this);
        billRecordAdapter.setNewData();
        billRecordLV.setAdapter(billRecordAdapter);
    }

    @Override
    protected void getDataFormWeb() {

    }

    @Override
    protected void debugShow() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_bill_record;
    }
}
