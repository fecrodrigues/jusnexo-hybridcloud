import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICredential, defaultValue } from 'app/shared/model/credential.model';

export const ACTION_TYPES = {
  FETCH_CREDENTIAL_LIST: 'credential/FETCH_CREDENTIAL_LIST',
  FETCH_CREDENTIAL: 'credential/FETCH_CREDENTIAL',
  CREATE_CREDENTIAL: 'credential/CREATE_CREDENTIAL',
  UPDATE_CREDENTIAL: 'credential/UPDATE_CREDENTIAL',
  PARTIAL_UPDATE_CREDENTIAL: 'credential/PARTIAL_UPDATE_CREDENTIAL',
  DELETE_CREDENTIAL: 'credential/DELETE_CREDENTIAL',
  RESET: 'credential/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICredential>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type CredentialState = Readonly<typeof initialState>;

// Reducer

export default (state: CredentialState = initialState, action): CredentialState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_CREDENTIAL_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CREDENTIAL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_CREDENTIAL):
    case REQUEST(ACTION_TYPES.UPDATE_CREDENTIAL):
    case REQUEST(ACTION_TYPES.DELETE_CREDENTIAL):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_CREDENTIAL):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_CREDENTIAL_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CREDENTIAL):
    case FAILURE(ACTION_TYPES.CREATE_CREDENTIAL):
    case FAILURE(ACTION_TYPES.UPDATE_CREDENTIAL):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_CREDENTIAL):
    case FAILURE(ACTION_TYPES.DELETE_CREDENTIAL):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_CREDENTIAL_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_CREDENTIAL):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_CREDENTIAL):
    case SUCCESS(ACTION_TYPES.UPDATE_CREDENTIAL):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_CREDENTIAL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_CREDENTIAL):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/credentials';

// Actions

export const getEntities: ICrudGetAllAction<ICredential> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CREDENTIAL_LIST,
    payload: axios.get<ICredential>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<ICredential> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CREDENTIAL,
    payload: axios.get<ICredential>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ICredential> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CREDENTIAL,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICredential> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CREDENTIAL,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<ICredential> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_CREDENTIAL,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICredential> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CREDENTIAL,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
