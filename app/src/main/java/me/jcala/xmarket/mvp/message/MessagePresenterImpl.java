package me.jcala.xmarket.mvp.message;

import android.content.Context;
import android.net.Uri;

import me.jcala.xmarket.R;
import me.jcala.xmarket.data.dto.MsgDto;
import me.jcala.xmarket.data.dto.Result;
import me.jcala.xmarket.data.pojo.Message;
import me.jcala.xmarket.view.RecyclerCommonAdapter;
import me.jcala.xmarket.view.RecyclerViewHolder;

public class MessagePresenterImpl implements MessagePresenter,MessageModel.onMessageListener {

    private Context context;
    private MessageModel model;
    private MessageView view;
    private int allMsgNum=96;

    public MessagePresenterImpl(Context context, MessageView view) {
        this.context = context;
        this.view = view;
        this.model=new MessageModelImpl();
    }

    @Override
    public void gainMessages() {
        model.executeMsgReq(this,allMsgNum);
    }

    @Override
    public void onComplete(Result<MsgDto> result) {
        if (!resultHandler(result)){
            return;
        }
        if (result.getData().getAllNum() <= allMsgNum){
            return;
        }

        RecyclerCommonAdapter<?> adapter=new RecyclerCommonAdapter<Message>(context,result.getData().getMsgs(), R.layout.message_item) {
            @Override
            public void convert(RecyclerViewHolder viewHolder, Message item) {
                if (item.getKind()==0){
                   viewHolder.setLineBgColor(R.id.message_kind_bg,context.getResources().getColor(R.color.md_red_300));
                    viewHolder.setText(R.id.message_kind_title,"已确认，请尽快联系卖家");
                }
                viewHolder.setFrescoImg(R.id.message_user_avatar,Uri.parse(item.getUserAvatar()));
                viewHolder.setFrescoImg(R.id.message_trade_img,Uri.parse(item.getTradeImg()));
                viewHolder.setText(R.id.message_user_phone,item.getUserPhone());
                viewHolder.setText(R.id.message_user_name,item.getUsername());
            }
        };
        view.whenNeedUpdateMsgList(adapter);
    }
    private boolean resultHandler(Result<?> result){
        if (result==null){
            return false;
        }
        switch (result.getCode()) {
            case 100:
                return true;
            case 99:
                return false;
            default:
                return false;
        }
    }
}
