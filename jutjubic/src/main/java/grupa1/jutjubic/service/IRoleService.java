package grupa1.jutjubic.service;

import grupa1.jutjubic.model.Role;

import java.util.List;

public interface IRoleService {
    Role findById(Long id);
    List<Role> findAll();
    List<Role> findByName(String name);
}
