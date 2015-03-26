/*
 *
 *  Copyright (c) 2014-2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.event.output.adapter.ui.UIOutputCallbackControllerService;

import javax.websocket.CloseReason;
import javax.websocket.Session;

/**
 * Interface for subscription and un-subscription for web socket
 */

public class SubscriptionEndpoint {

    private static final Log log = LogFactory.getLog(SubscriptionEndpoint.class);
    protected UIOutputCallbackControllerService uiOutputCallbackControllerService;

    public SubscriptionEndpoint() {
        uiOutputCallbackControllerService = (UIOutputCallbackControllerService) PrivilegedCarbonContext
                .getThreadLocalCarbonContext()
                .getOSGiService(UIOutputCallbackControllerService.class);
    }

    /**
     * Web socket onClose - Remove the registered sessions
     *
     * @param session - Users registered session.
     * @param reason  - Status code for web-socket close.
     * @param streamName - StreamName extracted from the ws url.
     * @param version - Version extracted from the ws url.
     * @param tenantId - Users tenantId.
     * @return
     */
    public void onClose (Session session, CloseReason reason, String streamName, String version, int tenantId) {
        if (log.isDebugEnabled()) {
            log.debug("Closing a WebSocket due to "+reason.getReasonPhrase()+", for session ID:"+session.getId()+", for request URI - "+session.getRequestURI());
        }
         uiOutputCallbackControllerService.unsubscribeWebsocket(tenantId, streamName, version, session);
    }

    /**
     * Web socket onError - Remove the registered sessions
     *
     * @param session - Users registered session.
     * @param throwable  - Status code for web-socket close.
     * @param streamName - StreamName extracted from the ws url.
     * @param version - Version extracted from the ws url.
     * @param tenantId - Users tenantId.
     * @return
     */
    public void onError (Session session, Throwable throwable, String streamName, String version, int tenantId) {
        log.error("Error occurred in session ID: "+session.getId()+", for request URI - "+session.getRequestURI()+", "+throwable.getMessage(),throwable);
          uiOutputCallbackControllerService.unsubscribeWebsocket(tenantId, streamName, version, session);
    }



}
