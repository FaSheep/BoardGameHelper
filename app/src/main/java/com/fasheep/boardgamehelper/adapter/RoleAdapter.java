package com.fasheep.boardgamehelper.adapter;

import android.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.fasheep.boardgamehelper.R;
import com.fasheep.boardgamehelper.Resource;
import com.fasheep.boardgamehelper.core.Role;
import com.fasheep.boardgamehelper.core.RoomManager;

import java.util.regex.Pattern;

public class RoleAdapter extends RecyclerView.Adapter {
    private static final int ITEM_TYPE_ROLE = 0;
    private static final int ITEM_TYPE_ADD = 1;
    private final String id;


    static class RoleViewHolder extends RecyclerView.ViewHolder {
        final TextView roleName;
        final ImageView roleImage;
        final EditText roleNum;
        final Button numAdd, numMinus;

        public RoleViewHolder(@NonNull View itemView) {
            super(itemView);
            roleImage = itemView.findViewById(R.id.roleImage);
            roleName = itemView.findViewById(R.id.roleName);
            roleNum = itemView.findViewById(R.id.roleNumber);
            numAdd = itemView.findViewById(R.id.numAdd);
            numMinus = itemView.findViewById(R.id.numMinus);
        }
    }

    static class AddViewHolder extends RecyclerView.ViewHolder {

        public AddViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public RoleAdapter(String id) {
        this.id = id;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.role_item, parent, false);
            holder = new RoleViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_item, parent, false);
            holder = new AddViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RoleViewHolder) {
            RoleViewHolder roleViewHolder = (RoleViewHolder) holder;
            Role role = RoomManager.getRoom(id).getRole(position);
            roleViewHolder.roleName.setText(role.getName());
            roleViewHolder.roleNum.setText(String.valueOf(role.getNumber()));
            roleViewHolder.roleNum.setHint(String.valueOf(role.getDefNumber()));
            roleViewHolder.roleImage.setImageResource(Resource.getID(RoomManager.getRoom(id).getRole(position).getImagePath()));
            roleViewHolder.numMinus.setOnClickListener(view -> editNum(roleViewHolder, -1, position, false));
            roleViewHolder.numAdd.setOnClickListener(view -> editNum(roleViewHolder, 1, position, false));
            roleViewHolder.roleNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editNum(roleViewHolder, null, roleViewHolder.getAdapterPosition(), true);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            roleViewHolder.itemView.setOnLongClickListener(view -> {
                AlertDialog.Builder removeRoleDialog = new AlertDialog.Builder(roleViewHolder.itemView.getContext());
                removeRoleDialog.setTitle(view.getContext().getString(R.string.delete_text));
                removeRoleDialog.setPositiveButton(view.getContext().getText(R.string.confirm), (dialogInterface, i) -> {
                    RoomManager.getRoom(id).removeRole(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());
                });
                removeRoleDialog.setNegativeButton(view.getContext().getText(R.string.cancel), (dialogInterface, i) -> {
                });
                removeRoleDialog.show();
                return true;
            });
        } else if (holder instanceof AddViewHolder) {
            AddViewHolder addViewHolder = (AddViewHolder) holder;
            addViewHolder.itemView.setOnClickListener(view -> {
                EditText roleName = new EditText(addViewHolder.itemView.getContext());
                AlertDialog.Builder inputDialog = new AlertDialog.Builder(addViewHolder.itemView.getContext());
                inputDialog.setTitle(view.getContext().getString(R.string.add_role_text));
                inputDialog.setView(roleName);
                inputDialog.setPositiveButton(view.getContext().getText(R.string.confirm), (dialogInterface, i) -> {
                    String name = roleName.getText().toString();
                    if (Pattern.compile("[;,=\"\\[\\]()/@:<>{}\n\r\t']").matcher(name).find()) {
                        Toast.makeText(view.getContext(), R.string.invalid_name, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    RoomManager.getRoom(id).addRole(roleName.getText().toString(), 1, "defRoleImage");
                    notifyItemInserted(getItemCount());
                });
                inputDialog.setNegativeButton(view.getContext().getText(R.string.cancel), (dialogInterface, i) -> {
                });
                inputDialog.show();
            });

        }
    }

    private void editNum(RoleViewHolder holder, @Nullable Integer i, int position, boolean replace) {
        String numText = holder.roleNum.getText().toString();
        if ("".equals(numText)) {
            numText = holder.roleNum.getHint().toString();
        }
        if (position < RoomManager.getRoom(id).getNumOfRoles()) {
            if (replace) {
                RoomManager.getRoom(id).getRole(position).setNumber(Integer.parseInt(numText));
            } else if (i != null) {
                int num = Integer.parseInt(numText);
                num = Math.max(num + i, 0);
                RoomManager.getRoom(id).getRole(position).setNumber(num);
                holder.roleNum.setText(String.valueOf(num));
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position < RoomManager.getRoom(id).getNumOfRoles() ? ITEM_TYPE_ROLE : ITEM_TYPE_ADD;
    }

    @Override
    public int getItemCount() {
        return RoomManager.getRoom(id).getNumOfRoles() + 1;
    }
}
