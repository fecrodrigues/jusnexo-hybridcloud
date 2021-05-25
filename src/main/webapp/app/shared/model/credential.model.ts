import { IClient } from 'app/shared/model/client.model';

export interface ICredential {
  id?: number;
  password?: string;
  username?: string;
  client?: IClient | null;
}

export const defaultValue: Readonly<ICredential> = {};
