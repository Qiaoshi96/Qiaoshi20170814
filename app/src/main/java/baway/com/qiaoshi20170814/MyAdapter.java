package baway.com.qiaoshi20170814;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaoshion 2017/8/14.
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<User.美女Bean> list;
    private List<Integer> mHeights;
    public MyAdapter(List<User.美女Bean> list) {
        this.list = list;
    }
    OnItemClickListener listener;//定义监听事件

    /**
     * 设置监听事件
     *
     * @param listener
     */
    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

//    设置瀑布流
        public void  getRandomHeight(List<User.美女Bean> mList){
            mHeights = new ArrayList<>();
            for(int i=0; i < mList.size();i++){
                //随机的获取一个范围为200-600直接的高度
                mHeights.add((int)(200+Math.random()*100));
            }
        }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder){
            MyViewHolder my= (MyViewHolder) holder;
//            设置属性动画
            ObjectAnimator animator = ObjectAnimator.ofFloat(my.imageView, "alpha", 0f, 1f);
            animator.setDuration(3000);
            animator.start();
//            设置瀑布流
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            layoutParams.height = mHeights.get(position);
            holder.itemView.setLayoutParams(layoutParams);

            x.image().bind(my.imageView,list.get(position).getImg());

            my.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!=null){
                        listener.onImageClick(view,position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{
        @ViewInject(R.id.imgs)
        ImageView imageView;
        public MyViewHolder(View itemView) {
            super(itemView);
            x.view().inject(this,itemView);
        }
    }
//    定义条目点击的监听
    /**
     * 点击事件回掉接口
     */
    public interface OnItemClickListener {
        void onImageClick(View view, int pos);

    }
}

