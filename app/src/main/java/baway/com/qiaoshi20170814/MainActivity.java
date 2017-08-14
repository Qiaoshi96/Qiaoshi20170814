package baway.com.qiaoshi20170814;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/*
qiaoshi 2017 08 14
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements XRecyclerView.LoadingListener,MyAdapter.OnItemClickListener{
    private String path="http://c.3g.163.com/recommend/getChanListNews?channel=T1456112189138&size=20&passport=&devId=1uuFYbybIU2oqSRGyFrjCw%3D%3D&lat=%2F%2FOm%2B%2F8ScD%2B9fX1D8bxYWg%3D%3D&lon=LY2l8sFCNzaGzqWEPPgmUw%3D%3D&version=9.0&net=wifi&ts=1464769308&sign=bOVsnQQ6gJamli6%2BfINh6fC%2Fi9ydsM5XXPKOGRto5G948ErR02zJ6%2FKXOnxX046I&encryption=1&canal=meizu_store2014_news&mac=sSduRYcChdp%2BBL1a9Xa%2F9TC0ruPUyXM4Jwce4E9oM30%3D";
    @ViewInject(R.id.xrecyc)
    XRecyclerView recyclerView;
    private List<User.美女Bean> list;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        list=new ArrayList<>();
        boolean b = checkNet();
        if (!b) {
//            使用对话框判断是否可以操作
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("是否去设置网络");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                    使用隐士跳转自动跳转到设置网络界面
                    Toast.makeText(MainActivity.this, "设置网络！", Toast.LENGTH_LONG).show();
//                    Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
//                    startActivity(intent);
                    return;
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(MainActivity.this, "查看本地", Toast.LENGTH_LONG).show();
                }
            });
            builder.create();
            builder.show();
        }else {
            adapter = new MyAdapter(list);
            recyclerView.setLoadingListener(this);
            recyclerView.setLoadingMoreEnabled(true);
            StaggeredGridLayoutManager manager1 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager1);
            recyclerView.setAdapter(adapter);
//        设置条目监听
            adapter.setListener(this);
            getDate();
            Toast.makeText(this, "网络正处于连接中", Toast.LENGTH_LONG).show();
        }
    }

    public void getDate() {
        RequestParams params = new RequestParams(path);
        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                User user = gson.fromJson(result, User.class);
                list.addAll(user.get美女());
                adapter.getRandomHeight(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
            recyclerView.refreshComplete();
                recyclerView.loadMoreComplete();
            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }
//实现刷新
    @Override
    public void onRefresh() {
        list.clear();
        getDate();
        Toast.makeText(MainActivity.this,"上拉刷新了",Toast.LENGTH_SHORT).show();
    }
//加载跟多
    @Override
    public void onLoadMore() {
        getDate();
        Toast.makeText(MainActivity.this,"分页加载了",Toast.LENGTH_SHORT).show();
    }
//    实现底部分页加载


    @Override
    public void onImageClick(View view, int pos) {
        Toast.makeText(MainActivity.this,"点击了"+pos,Toast.LENGTH_SHORT).show();
    }
    //    判断当前的网络状态如果有网的话就返回true如果没有网的话就返回false
    private boolean checkNet() {
        ConnectivityManager conn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = conn.getActiveNetworkInfo();
        if (net != null && net.isConnected()) {
            return true;
        }
        return false;
    }
}
