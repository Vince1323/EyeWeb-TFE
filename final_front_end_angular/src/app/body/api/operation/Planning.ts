export interface Planning {
  id: number;
  planningDate: string;
  eyeSide: 'OD' | 'OS' | 'OU';
  eyeDescription: string;
  notes: string;
  patientId: number;
  createdBy: string;
  createdAt: string;
  modifiedBy: string;
  modifiedAt: string;
}
