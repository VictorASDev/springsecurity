package tech.buildrun.springsecurity.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    private String name;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public enum Values {
        BASIC(1L),
        ADMIN(2L);

        long roleid;
        Values(long roleid) {
            this.roleid = roleid;
        }

        public long getRoleid() {
            return roleid;
        }
    }
}
