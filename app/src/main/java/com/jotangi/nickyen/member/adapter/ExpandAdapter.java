package com.jotangi.nickyen.member.adapter;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jotangi.nickyen.R;
import com.jotangi.nickyen.member.model.FAQBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * /\/\/\/\/\/\/\/\/\/\/\/\/\/
 * Created by Android Studio.
 * Name: N!ck
 * Date: 2021/10/7
 * \/\/\/\/\/\/\/\/\/\/\/\/\/\
 **/
public class ExpandAdapter extends RecyclerView.Adapter<ExpandAdapter.ViewHolder>
{
    private ArrayList<FAQBean> faqArrayList;
    private Context context;
    boolean status = true;

    public ExpandAdapter(final ArrayList<FAQBean> faqArrayList, final Context context)
    {
        this.faqArrayList = faqArrayList;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ConstraintLayout expandableLayout;
        TextView titleTextView, plotTextView;
        ImageView imgArrow;

        public ViewHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            plotTextView = itemView.findViewById(R.id.plotTextView);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            imgArrow = itemView.findViewById(R.id.imgArrow);

            titleTextView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    FAQBean data = faqArrayList.get(getBindingAdapterPosition());
                    data.setExpanded(!data.isExpanded());
                    notifyItemChanged(getBindingAdapterPosition());
                    status = false;
                }
            });
        }
    }

    @NonNull
    @NotNull
    @Override
    public ExpandAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        return new ExpandAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ExpandAdapter.ViewHolder holder, int position)
    {
        FAQBean faqBean = faqArrayList.get(position);
        holder.titleTextView.setText(faqBean.getQuestiontypeName());
        holder.plotTextView.setText(faqBean.getQuestionSubject());

        boolean isExpanded = faqArrayList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.plotTextView.setOnClickListener(v -> dialogFAQ(faqBean));

        if (faqBean.isExpanded() && !status)
        {
            ObjectAnimator animator = ObjectAnimator.ofFloat(holder.imgArrow, "rotation", -90f, 0f);
            animator.setDuration(500);
            animator.start();
        } else if (!faqBean.isExpanded() && !status)
        {
            ObjectAnimator animator = ObjectAnimator.ofFloat(holder.imgArrow, "rotation", 0f, -90f);
            animator.setDuration(500);
            animator.start();
        }
    }

    @Override
    public int getItemCount()
    {
        return faqArrayList.size();
    }

    private void dialogFAQ(FAQBean faqBean)
    {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_faq);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        TextView txtTitle = dialog.findViewById(R.id.tvSubTitle);
        txtTitle.setText(faqBean.getQuestionSubject());
        TextView txtContent = dialog.findViewById(R.id.tvContent);
        txtContent.setText(faqBean.getQuestionDescription());

        Button btnEnter = dialog.findViewById(R.id.btn_confirm);
        btnEnter.setOnClickListener(v ->
        {
            if (dialog != null)
            {
                dialog.dismiss();
            }
        });
    }
}
