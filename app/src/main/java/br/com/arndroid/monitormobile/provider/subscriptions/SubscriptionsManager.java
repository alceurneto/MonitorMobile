package br.com.arndroid.monitormobile.provider.subscriptions;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.arndroid.monitormobile.provider.Contract;

public class SubscriptionsManager {

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionsManager.class);

    final private Context mContext;

    public SubscriptionsManager(Context context) {
        mContext = context;
    }

    public SubscriptionsEntity subscriptionFromId(Long id) {
        Cursor c = null;
        try {
            c = mContext.getContentResolver().query(ContentUris.withAppendedId(
                    Contract.Subscriptions.CONTENT_URI, id), null, null, null, null);
            if(c.getCount() > 0) {
                c.moveToFirst();
                return SubscriptionsEntity.fromCursor(c);
            } else {
                return null;
            }
        } finally {
            if (c != null) c.close();
        }
    }

    public void refresh(SubscriptionsEntity entity) {

        entity.validateOrThrow();

        final Long id = entity.getId();
        if(id == null) {
            LOG.trace("About to insert entity={} with Uri={}", entity, Contract.Subscriptions.CONTENT_URI);
            final Uri resultUri = mContext.getContentResolver().insert(Contract.Subscriptions.CONTENT_URI,
                    entity.toContentValues());
            entity.setId(Long.parseLong(resultUri.getLastPathSegment()));
            LOG.trace("Entity inserted with id={}", entity.getId());
        } else {
            mContext.getContentResolver().update(ContentUris.withAppendedId(Contract.Subscriptions.CONTENT_URI, id),
                    entity.toContentValues(), null, null);
            LOG.trace("Entity updated: {}", entity);
        }
    }

    public void remove(Long id) {
        mContext.getContentResolver().delete(ContentUris.withAppendedId(Contract.Subscriptions.CONTENT_URI,
                id), null, null);
    }

    public boolean entityWillCauseConstraintViolation(SubscriptionsEntity entity) {
        Cursor c = null;
        try {
            c = mContext.getContentResolver().query(Contract.Subscriptions.CONTENT_URI,
                    Contract.Subscriptions.ID_PROJECTION, Contract.Subscriptions.ACRONYM_ID_AND_SUBSCRIBER_ID_SELECTION,
                    new String[]{entity.getAcronymId(), entity.getSubscriberId().toString()}, null);
            if (c.getCount() > 0) {
                c.moveToFirst();
                final SubscriptionsEntity resultEntity = SubscriptionsEntity.fromCursor(c);
                return !resultEntity.getId().equals(entity.getId());
            }
        } finally {
            if (c != null) c.close();
        }
        return false;
    }
}