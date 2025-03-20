package com.example.project_prm.Repository;

import android.content.Context;
import com.example.project_prm.Dao.RoleDao;
import com.example.project_prm.Database.ClothingDatabase;
import com.example.project_prm.Entities.Role;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RoleRepository {
    private final RoleDao roleDao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public RoleRepository(Context context) {
        ClothingDatabase db = ClothingDatabase.getInstance(context);
        roleDao = db.roleDao();
    }

    public void insert(Role role) {
        executorService.execute(() -> roleDao.insert(role));
    }

    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }
}
