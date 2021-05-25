import { IClient } from 'app/shared/model/client.model';

export interface IClientRating {
  id?: number;
  score?: number;
  description?: string;
  client?: IClient | null;
  clientEvaluator?: IClient | null;
}

export const defaultValue: Readonly<IClientRating> = {};
