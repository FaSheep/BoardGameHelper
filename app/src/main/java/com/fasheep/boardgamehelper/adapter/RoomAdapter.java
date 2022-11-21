package com.fasheep.boardgamehelper.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.fasheep.boardgamehelper.R;
import com.fasheep.boardgamehelper.Resource;
import com.fasheep.boardgamehelper.core.RoomManager;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {
    private OnItemClickListener onItemClickListener;

    static class RoomViewHolder extends RecyclerView.ViewHolder {
        private final ImageView roomImage;
        private final TextView roomName;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomImage = itemView.findViewById(R.id.roomImage);
            roomName = itemView.findViewById(R.id.roomName);
        }
    }

    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(view -> {
                onItemClickListener.onItemClick(  view, position);
            });
        }
        holder.roomImage.setImageResource(Resource.getID(RoomManager.getRoom(String.valueOf(position)).getRoomImagePath()));
        holder.roomName.setText(RoomManager.getRoom(String.valueOf(position)).getRoomName());
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return RoomManager.getNumOfRooms();
    }
}
