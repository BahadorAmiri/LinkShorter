package ir.atgroup.linkshorter.adapters;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.atgroup.linkshorter.DaTaBaCe.MyDaTaBaSe;
import ir.atgroup.linkshorter.R;
import ir.atgroup.linkshorter.models.URLs;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Holder> {

    MyDaTaBaSe myDaTaBaSe;
    Context context;
    List<URLs> urlModels;

    public HistoryAdapter(Context context, MyDaTaBaSe myDaTaBaSe) {
        this.context = context;
        this.myDaTaBaSe = myDaTaBaSe;
        urlModels = myDaTaBaSe.dao().getAll();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(context).inflate(R.layout.activity_history_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        holder.link.setText(urlModels.get(position).getUrl());
        holder.linksh.setText(urlModels.get(position).getShortened());

        holder.item_copy.setOnClickListener(view -> {

            PopupMenu popupMenu = new PopupMenu(context, holder.item_copy);
            popupMenu.getMenuInflater().inflate(R.menu.activity_history_popup_copy, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {

                if (menuItem.getItemId() == R.id.activity_history_popup_copy_main_link) {

                    ClipData clipData = ClipData.newPlainText(context.getString(R.string.app_name), urlModels.get(position).getUrl());
                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(context, context.getString(R.string.main_link_copied), Toast.LENGTH_SHORT).show();

                } else if (menuItem.getItemId() == R.id.activity_history_popup_copy_short_link) {

                    ClipData clipData = ClipData.newPlainText(context.getString(R.string.app_name), urlModels.get(position).getShortened());
                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(context, context.getString(R.string.short_link_copied), Toast.LENGTH_SHORT).show();

                }

                return false;
            });
            popupMenu.show();


        });

        holder.item_share.setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, urlModels.get(position).getShortened());
            intent.setType("text/*");
            Intent chooser = Intent.createChooser(intent, "Select App");
            context.startActivity(chooser);

        });

        holder.item_delete.setOnClickListener(view -> {

            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setContentView(R.layout.dialog_history_delete);
            dialog.setCancelable(false);
            AppCompatButton delete = dialog.findViewById(R.id.delete);
            delete.setOnClickListener(view1 -> {
                dialog.dismiss();
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.to_right);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        myDaTaBaSe.dao().delete(urlModels.get(position));
                        urlModels.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, context.getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                holder.itemView.startAnimation(animation);
            });

            AppCompatButton nodelete = dialog.findViewById(R.id.nodelete);
            nodelete.setOnClickListener(view12 -> dialog.dismiss());
            dialog.show();

        });

    }

    @Override
    public int getItemCount() {
        return urlModels.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {

        AppCompatTextView link, linksh;
        AppCompatImageView item_share, item_copy, item_delete;

        public Holder(@NonNull View itemView) {
            super(itemView);

            link = itemView.findViewById(R.id.link);
            linksh = itemView.findViewById(R.id.linksh);

            item_copy = itemView.findViewById(R.id.copy_link);
            item_delete = itemView.findViewById(R.id.item_delete);
            item_share = itemView.findViewById(R.id.item_share);

        }
    }

}
