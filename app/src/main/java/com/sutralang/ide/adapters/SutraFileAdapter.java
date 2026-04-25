package com.sutralang.ide.adapters;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.sutralang.ide.R;
import com.sutralang.ide.models.SutraFile;
import java.util.ArrayList;
import java.util.List;

/**
 * SutraFileAdapter - RecyclerView adapter for displaying list of .sutra files
 */
public class SutraFileAdapter extends RecyclerView.Adapter<SutraFileAdapter.FileViewHolder> {
    private List<String> fileList;
    private OnFileClickListener listener;
    
    public interface OnFileClickListener {
        void onFileClick(String fileName);
        void onFileDelete(String fileName);
        void onFileRename(String fileName);
    }
    
    public SutraFileAdapter(OnFileClickListener listener) {
        this.fileList = new ArrayList<>();
        this.listener = listener;
    }
    
    public void setFileList(List<String> files) {
        this.fileList = files;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sutra_file, parent, false);
        return new FileViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        String fileName = fileList.get(position);
        holder.bind(fileName, listener);
    }
    
    @Override
    public int getItemCount() {
        return fileList.size();
    }
    
    /**
     * ViewHolder for individual file items
     */
    public static class FileViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView fileNameTextView;
        private String fileName;
        private OnFileClickListener listener;
        
        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.file_name_text);
            itemView.setOnCreateContextMenuListener(this);
        }
        
        public void bind(String fileName, OnFileClickListener listener) {
            this.fileName = fileName;
            this.listener = listener;
            
            // Remove .sutra extension for display
            String displayName = fileName.endsWith(".sutra") 
                    ? fileName.substring(0, fileName.length() - 6) 
                    : fileName;
            fileNameTextView.setText(displayName);
            
            // Click to open file
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onFileClick(fileName);
                }
            });
        }
        
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, 1, 0, "Delete").setOnMenuItemClickListener(item -> {
                if (listener != null) {
                    listener.onFileDelete(fileName);
                }
                return true;
            });
            
            menu.add(0, 2, 0, "Rename").setOnMenuItemClickListener(item -> {
                if (listener != null) {
                    listener.onFileRename(fileName);
                }
                return true;
            });
        }
    }
}
