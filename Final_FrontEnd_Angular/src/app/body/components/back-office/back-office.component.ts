import { Component, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../service/UserService';
import { User } from '../../api/auth/User';
import { ConfirmationService, MessageService, SortEvent } from 'primeng/api';
import { Table } from 'primeng/table';

@Component({
  selector: 'back-office',
  templateUrl: './back-office.component.html',
  providers: [UserService, ConfirmationService, MessageService],
})
export class BackOfficeComponent implements OnInit {

  @ViewChild('dt2') dt2!: Table;

  users: User[] = [];
  filteredUsers: User[] = [];
  loading: boolean = true;
  isSorted: boolean = null;
  initialValue: User[] = [];
  query: boolean = false;

  message: string[] = ['Do you want to activate this account ?', 'Do you want to disable this account ?'];

  filters = {
    firstname: '',
    lastname: '',
    role: '',
    verified: true,
    validEmail: true
  };

  editingRoleUserId: number | null = null;
  selectedRole: string = '';

  availableRoles = [
    { label: 'Admin', value: 'ADMIN' },
    { label: 'User', value: 'USER' },
    { label: 'Secretary', value: 'SECRETARY' },
    { label: 'Doctor', value: 'DOCTOR' }
  ];

  constructor(
    private userService: UserService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.refreshUsers();
  }

  refreshUsers(callback?: () => void): void {
    this.loading = true;
    this.userService.getAllUsers().subscribe({
      next: (usersResp) => {
        this.users = usersResp;
        this.initialValue = [...usersResp];
        this.filteredUsers = [...usersResp];
        this.loading = false;
        if (callback) callback(); 
      },
      error: (err) => {
        console.error('❌ Failed to refresh users:', err);
        this.loading = false;
        if (callback) callback(); 
      }
    });
  }
  

  customSort(event: SortEvent): void {
    if (this.isSorted == null || this.isSorted === undefined) {
      this.isSorted = true;
      this.sortTableData(event);
    } else if (this.isSorted === true) {
      this.isSorted = false;
      this.sortTableData(event);
    } else {
      this.isSorted = null;
      this.filteredUsers = [...this.initialValue];
      this.dt2.reset();
    }
  }

  sortTableData(event: SortEvent): void {
    event.data.sort((data1, data2) => {
      const value1 = data1[event.field];
      const value2 = data2[event.field];
      let result = null;

      if (value1 == null && value2 != null) result = -1;
      else if (value1 != null && value2 == null) result = 1;
      else if (value1 == null && value2 == null) result = 0;
      else if (typeof value1 === 'string' && typeof value2 === 'string') result = value1.localeCompare(value2);
      else result = value1 < value2 ? -1 : value1 > value2 ? 1 : 0;

      return event.order * result;
    });
  }

  updateVerified(userId: number): void {
    const user = this.users.find(u => u.id === userId);
    if (!user) return;
  
    const idMessage = !user.verified ? 0 : 1;
  
    this.confirmationService.confirm({
      message: this.message[idMessage],
      header: 'Account verification',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-text',
      rejectButtonStyleClass: 'p-button-text p-button-text',
      acceptIcon: 'none',
      rejectIcon: 'none',
  
      accept: () => {
        this.query = true;
  
        // Adresse complète, ou adresse vide mais valide
        const safeAddress = user.address
        ? {
            id: user.address.id,
            version: user.address.version,
            deleted: user.address.deleted ?? false,
            street: user.address.street ?? '',
            streetNumber: user.address.streetNumber ?? '',
            boxNumber: user.address.boxNumber ?? '',
            zipCode: user.address.zipCode ?? '',
            city: user.address.city ?? '',
            country: user.address.country ?? '',
            createdBy: user.address.createdBy ?? 'System',
            createdAt: user.address.createdAt ?? new Date().toISOString(),
            modifiedBy: user.address.modifiedBy ?? 'System',
            modifiedAt: new Date().toISOString()
          }
        : {
            id: null,
            version: 1,
            deleted: false,
            street: '',
            streetNumber: '',
            boxNumber: '',
            zipCode: '',
            city: '',
            country: '',
            createdBy: 'System',
            createdAt: new Date().toISOString(),
            modifiedBy: 'System',
            modifiedAt: new Date().toISOString()
          };
  
        const updatedUser: User = {
          ...user,
          verified: !user.verified,
          address: safeAddress
        };
  
        this.userService.updateVerifiedUser(updatedUser).subscribe({
          next: () => {
            this.query = false;
            this.messageService.add({
              severity: 'success',
              summary: 'User Modified',
              detail: 'User data has been successfully updated.'
            });
            this.refreshUsers();
          },
          error: (err) => {
            this.query = false;
            console.error('❌ API error:', err);
            this.messageService.add({
              severity: 'error',
              summary: 'ERROR',
              detail: 'An error occurred while updating the user.'
            });
          }
        });
      }
    });
  }
  

  // Affiche les utilisateurs en fonction des filtres dynamiques
  applyFilters(): void {
    const { firstname, lastname, role, verified, validEmail } = this.filters;

    this.filteredUsers = this.users.filter(user => {
      const roleName = typeof user.role === 'string'
        ? user.role
        : user.role?.name ?? '';

      return (
        (!firstname || user.firstname?.toLowerCase().includes(firstname.toLowerCase())) &&
        (!lastname || user.lastname?.toLowerCase().includes(lastname.toLowerCase())) &&
        (!role || roleName.toLowerCase().includes(role.toLowerCase())) &&
        (verified === null || user.verified === verified) &&
        (validEmail === null || user.validEmail === validEmail)
      );
    });
  }

  onCheckboxChange(field: 'verified' | 'validEmail', value: boolean): void {
    this.filters[field] = value ? true : null;
    this.applyFilters();
  }

  resetFilters(): void {
    this.filters = {
      firstname: '',
      lastname: '',
      role: '',
      verified: true,
      validEmail: true
    };
    this.applyFilters();
  }

  getRoleLabel(role: any): string {
    const roleStr = typeof role === 'string' ? role : role?.name ?? 'Unknown';
    switch (roleStr) {
      case 'ADMIN': return 'ADMIN';
      case 'USER': return 'USER';
      case 'SECRETARY': return 'SECRETARY';
      default: return roleStr;
    }
  }

  startEditRole(user: User): void {
    this.editingRoleUserId = user.id;
    this.selectedRole = typeof user.role === 'string' ? user.role : user.role?.name ?? '';
  }

  askConfirmRoleUpdate(user: User): void {
    const currentRoleName = typeof user.role === 'string' ? user.role : user.role?.name ?? '';
  
    if (!this.selectedRole || this.selectedRole === currentRoleName) {
      return;
    }
  
    this.confirmationService.confirm({
      message: `Are you sure you want to change the role of ${user.firstname} ${user.lastname} to ${this.selectedRole}?`,
      header: 'Role Modification',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-success p-button-text',
      rejectButtonStyleClass: 'p-button-secondary p-button-text',
      acceptIcon: 'none',
      rejectIcon: 'none',
  
      accept: () => {
        this.confirmRoleUpdate(user);
      },
      reject: () => {
        this.editingRoleUserId = null;
        this.selectedRole = '';
      }
    });
  }
  
  confirmRoleUpdate(user: User): void {
    const currentRoleName = typeof user.role === 'string' ? user.role : user.role?.name ?? '';
  
    if (!this.selectedRole || this.selectedRole === currentRoleName) {
      this.editingRoleUserId = null;
      return;
    }
  
    this.query = true;
  
    this.userService.updateUserRole(user.id, this.selectedRole).subscribe({
      next: (updatedUser) => {
        this.editingRoleUserId = null;
        this.selectedRole = '';
  
        // Rafraîchit les utilisateurs, ET ensuite on ferme la progress bar
        this.userService.getAllUsers().subscribe({
          next: (usersResp) => {
            this.users = usersResp;
            this.initialValue = [...usersResp];
            this.filteredUsers = [...usersResp];
  
            this.messageService.add({
              severity: 'success',
              summary: 'Role Updated',
              detail: `Role updated to ${updatedUser.role.name ?? updatedUser.role}`
            });
  
            setTimeout(() => {
                this.query = false; 
              }, 500);  
            },
          error: (err) => {
            this.query = false;
            console.error('❌ Failed to refresh users:', err);
          }
        });
      },
      error: (err) => {
        this.query = false;
        console.error('❌ Error updating role:', err);
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: 'An error occurred while updating the role.'
        });
      }
    });
  }
  
   
}
