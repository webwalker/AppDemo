package com.webwalker.framework.dns.local;

import com.webwalker.framework.dns.Domain;
import com.webwalker.framework.dns.IResolver;
import com.webwalker.framework.dns.NetworkInfo;
import com.webwalker.framework.dns.Record;

import java.io.IOException;

/**
 * Created by bailong on 15/7/10.
 */
public final class HijackingDetectWrapper implements IResolver {
    private final Resolver resolver;

    public HijackingDetectWrapper(Resolver r) {
        this.resolver = r;
    }

    @Override
    public Record[] resolve(Domain domain, NetworkInfo info) throws IOException {
        Record[] records = resolver.resolve(domain, info);
        if (domain.hasCname) {
            boolean cname = false;
            for (Record r : records) {
                if (r.isCname()) {
                    cname = true;
                    break;
                }
            }
            if (!cname) {
                throw new DnshijackingException(domain.domain,
                        resolver.address.getHostAddress());
            }
        }
        if (domain.maxTtl != 0) {
            for (Record r : records) {
                if (!r.isCname()) {
                    if (r.ttl > domain.maxTtl) {
                        throw new DnshijackingException(domain.domain,
                                resolver.address.getHostAddress(), r.ttl);
                    }
                }
            }
        }
        return records;
    }
}
