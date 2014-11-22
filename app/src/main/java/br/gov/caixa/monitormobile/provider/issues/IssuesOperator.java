package br.gov.caixa.monitormobile.provider.issues;

import android.content.UriMatcher;
import android.net.Uri;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.gov.caixa.monitormobile.provider.BaseProviderOperator;
import br.gov.caixa.monitormobile.provider.Contract;
import br.gov.caixa.monitormobile.provider.OperationParameters;
import br.gov.caixa.monitormobile.provider.Provider;
import br.gov.caixa.monitormobile.utils.UrisUtils;

public class IssuesOperator extends BaseProviderOperator {

    private static final Logger LOG = LoggerFactory.getLogger(IssuesOperator.class);

    // Safe change Issues.Uri: add line for a new uri.
    private static final int ISSUES_URI_MATCH = 1;
    private static final int ISSUES_ITEM_URI_MATCH = 2;

    public IssuesOperator() {
        UriMatcher matcher =  getUriMatcher();
        // Safe change Issues.Uri: add line for a new uri.
        matcher.addURI(Contract.Issues.CONTENT_URI.getAuthority(),
                UrisUtils.pathForUriMatcherFromUri(Contract.Issues.CONTENT_URI), ISSUES_URI_MATCH);
        matcher.addURI(Contract.Issues.CONTENT_URI.getAuthority(),
                UrisUtils.pathForUriMatcherFromUri(Contract.Issues.CONTENT_URI) + "/#",
                ISSUES_ITEM_URI_MATCH);
    }

    @Override
    public String getType(Uri uri) {
        switch (getUriMatcher().match(uri)) {
            // Safe change Issues.Uri: add line for a new uri.
            case ISSUES_URI_MATCH:
                return Contract.Issues.CONTENT_TYPE;
            case ISSUES_ITEM_URI_MATCH:
                return Contract.Issues.CONTENT_ITEM_TYPE;
            default:
                LOG.trace("Unknown uri in getType(Uri):{}", uri);
                return null;
        }
    }

    @Override
    public String tableName() {
        return Contract.Issues.TABLE_NAME;
    }

    @Override
    public boolean isOperationProhibitedForUri(int operation, Uri uri) {
        int match = getUriMatcher().match(uri);
        if(match == UriMatcher.NO_MATCH) throw new IllegalArgumentException("Unknown uri: " + uri);

        // Safe change Issues.Uri: evaluate line addition for new uri.
        switch (operation) {
            case QUERY_OPERATION:
                return false;
            case INSERT_OPERATION:
                return match != ISSUES_URI_MATCH;
            case UPDATE_OPERATION:
                return false;
            case DELETE_OPERATION:
                return false;
            default:
                throw new IllegalArgumentException("Unknown operation: " + operation);
        }
    }

    @Override
    public void onValidateParameters(int operation, Uri uri, OperationParameters parameters,
                                     Provider provider) {

        // Safe change Issues.Uri: evaluate line(s) addition for new uri.
        if (getUriMatcher().match(uri) == ISSUES_ITEM_URI_MATCH) {
            parameters.setSelection(Contract.Issues.ID_SELECTION);
            parameters.setSelectionArgs(new String[] {uri.getLastPathSegment()});
        }
    }
}