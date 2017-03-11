package com.example.itheimabog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by riggin on 2017/3/11.
 */

public class GirlAdapter extends BaseAdapter {
    Context mContext;
    List<ResultBean.ResultsBean> mList;

    public GirlAdapter(Context context, List<ResultBean.ResultsBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder hodler;
        if(convertView ==null){
            convertView= View.inflate(mContext,R.layout.item_girl,null);
            hodler = new ViewHolder(convertView);
            convertView.setTag(hodler);
        }else{
            hodler = (ViewHolder) convertView.getTag();
        }
        ResultBean.ResultsBean resultsBean = mList.get(position);
        hodler.mTextView.setText(resultsBean.getCreatedAt());
       // Picasso.with(mContext).load(resultsBean.getUrl()).into(hodler.mImageView);
        Picasso.with(mContext).load(resultsBean.getUrl())
                .transform(new CropCircleTransformation())
                .into(hodler.mImageView);
        return convertView;
    }

    public class ViewHolder{
        ImageView mImageView;
        TextView mTextView;
        public  ViewHolder(View root){
            mImageView = (ImageView) root.findViewById(R.id.iv_gril);
            mTextView = (TextView) root.findViewById(R.id.tv_createdat);
        }
    }
}
