import dayjs from 'dayjs';
import { IChat } from 'app/shared/model/chat.model';

export interface IMessage {
  id?: number;
  description?: string | null;
  createdAt?: string | null;
  chat?: IChat | null;
}

export const defaultValue: Readonly<IMessage> = {};
